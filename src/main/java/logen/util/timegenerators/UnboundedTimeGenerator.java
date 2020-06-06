package logen.util.timegenerators;

import logen.util.Validations;

import java.time.LocalTime;

/**
 * A generator of either increasing or decreasing time values not bounded
 * by any ending time.
 */
public class UnboundedTimeGenerator extends AbstractTimeGenerator {
    private final TimeGenerationDirection direction;
    
    private UnboundedTimeGenerator(
            TimeGenerationDirection direction,
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        super(fromTime, wrapAroundTime, wrapToTime);
        this.direction = direction;
    }

    /**
     * Constructs a generator that generates increasing time values between
     * {@code wrapToTime} and {@code wrapAroundTime} inclusive starting from
     * {@code fromTime}.
     * If a time value generated is after {@code wrapAroundTime}, the time
     * value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param wrapAroundTime The latest time of all time values generated
     *                       and the time at which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                   wraps around
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} <= {@code fromTime} <=
     *   {@code wrapAroundTime}
     */
    public static UnboundedTimeGenerator forward(
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validations.requireNonNull(fromTime, wrapAroundTime, wrapToTime);
        Validations.requireInOrder(wrapToTime, fromTime, wrapAroundTime);

        return new UnboundedTimeGenerator(
                TimeGenerationDirection.FORWARD,
                fromTime,
                wrapAroundTime,
                wrapToTime
        );
    }

    /**
     * Constructs a generator that generates decreasing time values between
     * {@code wrapAroundTime} and {@code wrapToTime} inclusive starting from
     * {@code fromTime}.
     * If a time value generated is before {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * @param fromTime The latest time of the first time value generated
     * @param wrapAroundTime The earliest time of all time values generated
     *                       and the time which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                   wraps around
     * @return A generator of decreasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} <= {@code fromTime} <=
     *   {@code wrapAroundTime}
     */
    public static UnboundedTimeGenerator backward(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validations.requireNonNull(fromTime, wrapAroundTime, wrapToTime);
        Validations.requireInOrder(wrapAroundTime, fromTime, wrapToTime);

        return new UnboundedTimeGenerator(
                TimeGenerationDirection.BACKWARD,
                fromTime,
                wrapAroundTime,
                wrapToTime
        );
    }

    @Override
    public LocalTime generate() {
        switch(direction) {
            case FORWARD:
                return generateForward();
            case BACKWARD:
                return generateBackward();
            default:
                throw new AssertionError();
        }
    }

    private LocalTime generateForward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.plusSeconds(seconds);
        if (timeValue.isAfter(wrapAroundTime)) {
            timeValue = wrapToTime;
            return generateForward();
        }
        return timeValue;
    }

    private LocalTime generateBackward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.minusSeconds(seconds);
        if (timeValue.isBefore(wrapAroundTime)) {
            timeValue = wrapToTime;
            return generateBackward();
        }
        return timeValue;
    }

    /**
     * The direction which generation of time values proceeds, corresponding
     * to the consecutive change of magnitude in a stream of time values.
     */
    private enum TimeGenerationDirection {
        /**
         * Represents a generation direction where time values of increasing
         * magnitude are to be generated before experiencing a sharp drop in
         * magnitude from one time value to the next, after which the pattern
         * repeats.
         */
        FORWARD,
        /**
         * Represents a generation direction where time values of decreasing
         * magnitude are to be generated before experiencing a sharp increase
         * in magnitude from one time value to the next, after which the pattern
         * repeats.
         */
        BACKWARD
    }
}
