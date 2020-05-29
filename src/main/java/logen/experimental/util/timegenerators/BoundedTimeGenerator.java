package logen.experimental.util.timegenerators;

import java.time.LocalTime;

public class BoundedTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime toTime;
    private final int requiredGenerationCount;

    private BoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int requiredGenerationCount
    ) {
        super(fromTime, wrapAroundTime, baseTime);
        this.toTime = toTime;
        this.requiredGenerationCount = requiredGenerationCount;
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code requiredGenerationCount} between {@code fromTime}
     * and {@code toTime} inclusive.
     * If {@code fromTime} is after {@code toTime}, generation wraps around
     * {@code wrapAroundTime} and begins again from {@code baseTime}.
     * After {@code requiredGenerationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param wrapAroundTime The time at which generation wraps around
     * @param baseTime The time which generation begins after wrapping around
     *                 {@code wrapAroundTime}
     * @param requiredGenerationCount The number of time values the generator
     *                                will generate
     * @return
     */
    public static BoundedTimeGenerator wrap(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int requiredGenerationCount
    ) {
        return new BoundedTimeGenerator(
                fromTime,
                toTime,
                wrapAroundTime,
                baseTime,
                requiredGenerationCount
        );
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code requiredGenerationCount} between {@code fromTime}
     * and {@code toTime}.
     * After {@code requiredGenerationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param requiredGenerationCount The number of time values the generator
     *                                will generate
     * @return
     */
    public static BoundedTimeGenerator linear(
            LocalTime fromTime,
            LocalTime toTime,
            int requiredGenerationCount
    ) {
        return new BoundedTimeGenerator(
                fromTime,
                toTime,
                null,
                null,
                requiredGenerationCount
        );
    }

    @Override
    public LocalTime generate() {
        return null;
    }

    public void skip(int generationCount) {

    }
}
