package logen.util.timegenerators;

import logen.util.Validation;

import java.time.LocalTime;

public class BoundedTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime toTime;
    private final boolean requiresWrapAround;
    private boolean hasWrappedAround;

    private BoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        super(fromTime, wrapAroundTime, baseTime);
        this.toTime = toTime;
        requiresWrapAround = fromTime.isAfter(toTime);
        hasWrappedAround = false;
    }

    public static BoundedTimeGenerator between(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        Validation.requireNonNull(fromTime, toTime, wrapAroundTime, baseTime);
        Validation.requireInOrder(baseTime, fromTime, wrapAroundTime);

        return new BoundedTimeGenerator(
                fromTime,
                toTime,
                wrapAroundTime,
                baseTime
        );
    }

    @Override
    public LocalTime generate() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.plusSeconds(seconds);

        if (requiresWrapAround) {
            if (!hasWrappedAround) {
                if (timeValue.isAfter(wrapAroundTime)) {
                    hasWrappedAround = true;

                    timeValue = baseTime;
                    return generate();
                }
            } else {
                if (timeValue.isAfter(toTime)) {
                    return null;
                }
            }
        } else {
            if (timeValue.isAfter(toTime)) {
                return null;
            }
        }
        return timeValue;
    }
}
