package logen.experimental.util.timegenerators;

import java.time.LocalTime;

public class UnboundedTimeGenerator extends AbstractTimeGenerator {
    private final TimeGeneratorType type;
    
    private UnboundedTimeGenerator(
            TimeGeneratorType type,
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        super(fromTime, wrapAroundTime, baseTime);
        this.type = type;
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
    public static UnboundedTimeGenerator forward(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime baseTime) {
        return new UnboundedTimeGenerator(
                TimeGeneratorType.FORWARD,
                fromTime,
                wrapAroundTime,
                baseTime
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
    public static UnboundedTimeGenerator back(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        return new UnboundedTimeGenerator(
                TimeGeneratorType.BACKWARD,
                fromTime,
                wrapAroundTime,
                baseTime
        );
    }

    @Override
    public LocalTime generate() {
        long seconds = generateRandomSeconds();
        switch(type) {
            case FORWARD:
                currentTime = currentTime.plusSeconds(seconds);
                if (currentTime.isAfter(wrapAroundTime)) {
                    currentTime = baseTime;
                    return generate();
                }
                break;
            case BACKWARD:
                currentTime = currentTime.minusSeconds(seconds);
                if (currentTime.isBefore(wrapAroundTime)) {
                    currentTime = baseTime;
                    return generate();
                }
                break;
            default:
                throw new AssertionError();
        }
        return currentTime;
    }
}
