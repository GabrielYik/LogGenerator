package logen.util.timegenerators;

import logen.util.Validation;

import java.time.LocalTime;

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

    public static BoundedTimeGenerator between(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validation.requireNonNull(fromTime, toTime, wrapAroundTime, wrapToTime);
        Validation.requireInOrder(wrapToTime, fromTime, wrapAroundTime);

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
