package logen.experimental.util.timegenerators;

import java.time.LocalTime;

public class UnboundedTimeGenerator extends AbstractTimeGenerator {
    private final TimeGeneratorType type;

    private LocalTime timeValue;

    private UnboundedTimeGenerator(
            TimeGeneratorType type,
            LocalTime fromTime,
            LocalTime toTime
    ) {
        super(fromTime, toTime);
        this.type = type;
        this.timeValue = fromTime;
    }

    /**
     * Constructs a generator that generates increasing time values between
     * {@code fromTime} and {@code wrapAroundTime}.
     * If a time value generated is after {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime}.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param wrapAroundTime The time at which generation wraps around
     * @return A generator of increasing time values
     */
    public static UnboundedTimeGenerator forward(LocalTime fromTime, LocalTime wrapAroundTime) {
        return new UnboundedTimeGenerator(
                TimeGeneratorType.FORWARD,
                fromTime,
                wrapAroundTime
        );
    }

    /**
     * Constructs a generator that generates decreasing time values between
     * {@code fromTime} and {@code wrapAroundTime}.
     * If a time value generated is before {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime}.
     *
     * @param fromTime The latest time of the first time value generated
     * @param wrapAroundTime The time at which generation wraps around
     * @return A generator of decreasing time values
     */
    public static UnboundedTimeGenerator back(LocalTime fromTime, LocalTime wrapAroundTime) {
        return new UnboundedTimeGenerator(
                TimeGeneratorType.BACKWARD,
                fromTime,
                wrapAroundTime
        );
    }

    @Override
    public LocalTime generate() {
        long seconds = generateRandomSeconds();
        switch(type) {
            case FORWARD:
                timeValue = timeValue.plusSeconds(seconds);
                if (timeValue.isAfter(toTime)) {
                    timeValue = fromTime;
                    return generate();
                }
                break;
            case BACKWARD:
                timeValue = timeValue.minusSeconds(seconds);
                if (timeValue.isBefore(toTime)) {
                    timeValue = fromTime;
                    return generate();
                }
                break;
            default:
                throw new AssertionError();
        }
        return timeValue;
    }
}
