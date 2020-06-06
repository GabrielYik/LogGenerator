package logen.util.timegenerators;

import logen.util.RandomUtil;
import logen.util.Validations;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Supplier;

/**
 * A variant of {@link BoundedTimeGenerator} with one additional constraint
 * imposed: the generator has to generate a specified number of time values
 * before the specified ending time is reached.
 * This constraint is essential in satisfying user-specified spacing amounts
 * between logs.
 */
public class StrictTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime toTime;
    /**
     * The exact number of time values this must provide.
     */
    private final int generationCount;
    private final Supplier<Long> timeValueDeltas;

    private StrictTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime,
            int generationCount
    ) {
        super(fromTime, wrapAroundTime, wrapToTime);
        this.toTime = toTime;
        this.generationCount = generationCount;
        timeValueDeltas = generateTimeValueDeltas();
    }

    /**
     * Generates the time value deltas that determine how much each time
     * value generated will differ from the next.
     * Each time value is a random number of seconds apart from the next.
     *
     * This pre-emptive generation ensures that the generation quantity
     * of {@code generationCount} can be fulfilled.
     *
     * {@link AbstractTimeGenerator#generateRandomSeconds()} cannot be used
     * since use of that method will not guarantee that the specified
     * number of time values required to be generated {@code generationCount}
     * can be met.
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
            long timeAfterWrapAround = toTime.toSecondOfDay() - wrapToTime.toSecondOfDay();
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
     * quantity {@code generationCount} between {@code wrapToTime} and
     * {@code wrapAroundTime} inclusive starting from {@code fromTime} and
     * ending at {@code toTime}.
     * If a time value generated is after {@code wrapAroundTime}, the time
     * value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * If {@code fromTime} is after {@code toTime}, generation wraps around
     * {@code wrapAroundTime} and begins again from {@code wrapToTime}.
     *
     * If {@code fromTime} is before {@code toTime}, generation does not wrap
     * around and {@code wrapAroundTime} and {@code wrapToTime} are effectively
     * unused.
     *
     * After {@code generationCount} time values are generated, subsequent
     * time values generated will be discarded.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param wrapAroundTime The latest time of all time values generated
     *                       and the time at which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                   wraps around
     * @param generationCount The number of time values to be generated
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} <= {@code fromTime} <=
     *   {@code wrapAroundTime} and {@code wrapToTime} <= {@code toTime} <=
     *   {@code wrapAroundTime}
     */
    public static StrictTimeGenerator wrap(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime,
            int generationCount
    ) {
        Validations.requireNonNull(fromTime, toTime, wrapAroundTime, wrapToTime);
        Validations.requireInOrder(wrapToTime, fromTime, wrapAroundTime);
        Validations.requireInOrder(wrapToTime, toTime, wrapAroundTime);

        return new StrictTimeGenerator(
                fromTime,
                toTime,
                wrapAroundTime,
                wrapToTime,
                generationCount
        );
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code generationCount} between {@code wrapToTime} and
     * {@code wrapAroundTime} inclusive starting at {@code fromTime} and ending
     * at {@code toTime}.
     * If a time value generated is after {@code wrapAroundTime}, the time
     * value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * This is the variant of
     * {@link StrictTimeGenerator#wrap(LocalTime, LocalTime, LocalTime, LocalTime, int)}
     * which assumes that {@code fromTime} is guaranteed to be before
     * {@code toTime}.
     *
     * After {@code generationCount} time values are generated, subsequent
     * time values generated will be discarded.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of a time value generated
     * @param generationCount The number of time values to be generated
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if {@code fromTime} is not before or
     *   equal to {@code toTime}
     */
    public static StrictTimeGenerator linear(
            LocalTime fromTime,
            LocalTime toTime,
            int generationCount
    ) {
        Validations.requireNonNull(fromTime, toTime);
        Validations.requireInOrder(fromTime, toTime);

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
            timeValue = wrapToTime.plusSeconds(timeValueDelta - secondsBefore);
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
