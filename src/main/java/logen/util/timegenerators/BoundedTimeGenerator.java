package logen.util.timegenerators;

import logen.util.Validations;

import java.time.LocalTime;

/**
 * A generator of increasing time values bounded by a specified ending time.
 */
public class BoundedTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime toTime;
    private final boolean requiresWrapAround;
    private boolean hasWrappedAround;
    private boolean hasReachedBound;

    private BoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        super(fromTime, wrapAroundTime, wrapToTime);
        this.toTime = toTime;
        requiresWrapAround = fromTime.isAfter(toTime);
        hasWrappedAround = false;
        hasReachedBound = false;
    }

    /**
     * Constructs a generator that generates increasing time values between
     * {@code wrapToTime} and {@code wrapAroundTime} inclusive starting at
     * {@code fromTime} and ending at {@code toTime}.
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
     * When a time value generated is after {@code toTime} without wrapping
     * around required, or after wrapping around is required and done,
     * subsequent time values generated will be discarded.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param wrapAroundTime The latest time of all time values generated
     *                       and the time at which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                   wraps around
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} <= {@code fromTime} <=
     *   {@code wrapAroundTime} and {@code wrapToTime} <= {@code toTime} <=
     *   {@code wrapAroundTime}
     */
    public static BoundedTimeGenerator between(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validations.requireNonNull(fromTime, toTime, wrapAroundTime, wrapToTime);
        Validations.requireInOrder(wrapToTime, fromTime, wrapAroundTime);
        Validations.requireInOrder(wrapToTime, toTime, wrapAroundTime);

        return new BoundedTimeGenerator(
                fromTime,
                toTime,
                wrapAroundTime,
                wrapToTime
        );
    }

    @Override
    public LocalTime generate() {
        if (hasReachedBound) {
            return null;
        }

        long seconds = generateRandomSeconds();
        timeValue = timeValue.plusSeconds(seconds);

        if (requiresWrapAround) {
            if (!hasWrappedAround) {
                if (timeValue.isAfter(wrapAroundTime)) {
                    hasWrappedAround = true;

                    timeValue = wrapToTime;
                    return generate();
                }
            } else {
                if (!hasReachedBound && timeValue.isAfter(toTime)) {
                    hasReachedBound = true;
                    return null;
                }
            }
        } else {
            if (!hasReachedBound && timeValue.isAfter(toTime)) {
                hasReachedBound = true;
                return null;
            }
        }
        return timeValue;
    }
}
