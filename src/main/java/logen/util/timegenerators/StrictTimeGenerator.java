package logen.util.timegenerators;

import logen.util.RandomUtil;
import logen.util.Validation;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Supplier;

/**
 * A generator of time values that is bounded by a configurable overriding
 * latest time and generates increasing time values.
 * The overriding latest time only takes effective if it is before the
 * wrap around time. If it is after or equal to the wrap around time,
 * there is no override.
 * This specification of an overriding latest time allows for the constraint
 * of the generation count to a specified quantity.
 * This constraint is essential in satisfying user-specified spacing amounts
 * between logs.
 */
public class StrictTimeGenerator extends AbstractTimeGenerator {
    /**
     * The overriding latest time.
     */
    private final LocalTime toTime;
    /**
     * The exact generations this must provide.
     */
    private final int generationCount;
    private final Supplier<Long> timeValueDeltas;

    private StrictTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int generationCount
    ) {
        super(fromTime, wrapAroundTime, baseTime);
        this.toTime = toTime;
        this.generationCount = generationCount;
        timeValueDeltas = generateTimeValueDeltas();
    }

    /**
     * Generates the time value deltas that determine how much each time
     * value generated will differ from the next.
     * Each time value is a random number of seconds apart from the next.
     * This pre-emptive generation ensures that the generation quantity
     * of {@code generationCount} can be fulfilled.
     *
     * @return A supplier of the generated time value deltas
     */
    private Supplier<Long> generateTimeValueDeltas() {
        long seconds = computeSecondsBudget();
        List<Long> timeValueDeltas = distributeRandomly(seconds);
        return () -> timeValueDeltas.isEmpty() ? null : timeValueDeltas.remove(0);
    }

    /**
     * Computes the number of seconds within which generations of quantity
     * {@code generationCount} must take place.
     *
     * @return The number of seconds
     */
    private long computeSecondsBudget() {
        if (requiresWrapAround()) {
            long timeBeforeWrapAround = wrapAroundTime.toSecondOfDay() - fromTime.toSecondOfDay();
            long timeAfterWrapAround = toTime.toSecondOfDay() - baseTime.toSecondOfDay();
            return timeBeforeWrapAround + timeAfterWrapAround;
        } else {
            return toTime.toSecondOfDay() - fromTime.toSecondOfDay();
        }
    }

    private boolean requiresWrapAround() {
        return toTime.isBefore(fromTime);
    }

    private List<Long> distributeRandomly(long seconds) {
        return RandomUtil.distributeRandomly(seconds, generationCount);
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code generationCount} from {@code baseTime} to
     * {@code wrapAroundTime}.
     *
     * If {@code fromTime} is after {@code toTime}, generation wraps around
     * {@code wrapAroundTime} and begins again from {@code baseTime}.
     *
     * If {@code fromTime} is before {@code toTime}, generation does not wrap
     * around and {@code wrapAroundTime} and {@code baseTime} are effectively
     * unused.
     *
     * After {@code generationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The time that overrides {@code wrapAroundTime} as the
     *               latest time of a time value generated if it is after
     *               {@code fromTime}
     * @param wrapAroundTime The latest time of a time value generated
     *                       and the time at which generation wraps around
     * @param baseTime The time which generation starts from after generation
     *                 wraps around
     * @param generationCount The number of time values to be generated
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     */
    public static StrictTimeGenerator wrap(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int generationCount
    ) {
        Validation.requireNonNull(fromTime, toTime, wrapAroundTime, baseTime);
        Validation.requireInOrder(baseTime, fromTime, wrapAroundTime);

        return new StrictTimeGenerator(
                fromTime,
                toTime,
                wrapAroundTime,
                baseTime,
                generationCount
        );
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code generationCount} from {@code fromTime}
     * to {@code toTime}.
     *
     * This is the variant of {@link StrictTimeGenerator#wrap(LocalTime, LocalTime, LocalTime, LocalTime, int)}
     * which assumes that {@code fromTime} is guaranteed to be before
     * {@code toTime}.
     *
     * After {@code generationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of a time value generated
     * @param generationCount The number of time values to be generated
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if {@code fromTime} is not before
     *   {@code toTime}
     */
    public static StrictTimeGenerator linear(
            LocalTime fromTime,
            LocalTime toTime,
            int generationCount
    ) {
        Validation.requireNonNull(fromTime, toTime);
        Validation.requireInOrder(fromTime, toTime);

        return new StrictTimeGenerator(
                fromTime,
                toTime,
                computeUnreachableTime(toTime, 1),
                computeUnreachableTime(fromTime, -1),
                generationCount
        );
    }

    private static LocalTime computeUnreachableTime(LocalTime first, long offset) {
        return first.plusSeconds(offset);
    }

    @Override
    public LocalTime generate() {
        Long timeValueDelta = timeValueDeltas.get();
        LocalTime newTimeValue = timeValue.plusSeconds(timeValueDelta);
        if (newTimeValue.isAfter(wrapAroundTime)) {
            long secondsBefore = wrapAroundTime.toSecondOfDay() - timeValue.toSecondOfDay();
            timeValue = baseTime.plusSeconds(timeValueDelta - secondsBefore);
        } else {
            timeValue = newTimeValue;
        }
        return timeValue;
    }

    /**
     * Generates the next time values of quantity {@code skipCount}
     * and discards them.
     *
     * @param skipCount The number of time values to generate and
     *                  discard
     * @throws IllegalArgumentException if {@code skipCount} is negative
     */
    public void skip(int skipCount) {
        if (skipCount < 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < skipCount; i++) {
            timeValueDeltas.get();
        }
    }
}
