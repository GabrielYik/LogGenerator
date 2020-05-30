package logen.experimental.util.timegenerators;

import logen.experimental.util.RandomUtil;

import java.time.LocalTime;
import java.util.ArrayList;
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
public class BoundedTimeGenerator extends AbstractTimeGenerator {
    /**
     * The overriding latest time.
     */
    private final LocalTime toTime;
    /**
     * The exact generations this must provide.
     */
    private final int generationCount;
    private final Supplier<Long> timeValueDeltas;

    private BoundedTimeGenerator(
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

    private Supplier<Long> generateTimeValueDeltas() {
        long seconds = computeSeconds();
        List<Long> timeValueDeltas = distributeRandomly(seconds);
        return () -> timeValueDeltas.remove(0);
    }

    private long computeSeconds() {
        if (toTime.isBefore(fromTime)) {
            return wrapAroundTime.toSecondOfDay()
                    - fromTime.toSecondOfDay()
                    + toTime.toSecondOfDay()
                    - baseTime.toSecondOfDay();
        } else {
            return toTime.toSecondOfDay() - fromTime.toSecondOfDay();
        }
    }

    private List<Long> distributeRandomly(long seconds) {
        List<Long> distribution = distributeEqually(seconds);
        return RandomUtil.randomise(
                distribution,
                RandomUtil::chooseBetween,
                (a, b) -> a - b,
                Long::sum
        );
    }

    private List<Long> distributeEqually(long seconds) {
        List<Long> values = new ArrayList<>(generationCount);
        long value = seconds / generationCount;
        for (int i = 0; i < generationCount - 1; i ++) {
            values.add(value);
        }
        long used = values.stream().reduce(0L, Long::sum);
        values.add(seconds - used);
        return values;
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
     */
    public static BoundedTimeGenerator wrap(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int generationCount
    ) {
        return new BoundedTimeGenerator(
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
     * This is the variant of {@link BoundedTimeGenerator#wrap(LocalTime, LocalTime, LocalTime, LocalTime, int)}
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
     */
    public static BoundedTimeGenerator linear(
            LocalTime fromTime,
            LocalTime toTime,
            int generationCount
    ) {
        return new BoundedTimeGenerator(
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

    public void skip(int generationCount) {
        for (int i = 0; i < generationCount; i++) {
            timeValueDeltas.get();
        }
    }
}
