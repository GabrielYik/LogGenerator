package logen.experimental.generation.fixed.appliers;

import logen.experimental.generation.fixed.GroupFixture;
import logen.experimental.generation.fixed.Placeholder;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.group.GroupTimePeriod;
import logen.experimental.util.timegenerators.AbstractTimeGenerator;
import logen.experimental.util.timegenerators.FixedBoundedTimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * An applier that acts on a group by applying the time period attribute
 * defined on the group onto the logs in the group.
 */
public class GroupTimePeriodApplier extends GroupAttributeApplier {
    /**
     * One time skip since the earliest time has already been generated
     * and one time skip since the latest time has yet to be generated.
     */
    private static final int TIME_SKIP_OFFSET = 2;

    private final GroupTimePeriod timePeriod;

    private final List<LogSpec> logSpecs;
    private final List<Placeholder.Builder> placeholders;

    /**
     * Constructs a group time period applier.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     * @param timePeriod The time period defined on the logs in the group
     */
    public GroupTimePeriodApplier(
            GroupFixture.Builder groupFixture,
            GroupTimePeriod timePeriod
    ) {
        super(groupFixture);
        this.timePeriod = timePeriod;
        this.logSpecs = groupFixture.getLogSpecs();
        this.placeholders = groupFixture.getPlaceholders();
    }

    @Override
    public GroupFixture.Builder apply() {
        int logCount = computeLogCount();
        FixedBoundedTimeGenerator timeGenerator = new FixedBoundedTimeGenerator(
                timePeriod.getStartTime(),
                timePeriod.getEndTime(),
                logCount
        );

        List<Log> fixedLogs = new ArrayList<>();
        int totalCounter = logSpecs.size() + placeholders.size();
        int counterForLogSpecs = 0;
        int counterForPlaceholders = 0;
        for (int i = 1; i < totalCounter - 1; i++) {
            if (isPlaceholder(i)) {
                Placeholder.Builder placeholder = placeholders.get(counterForPlaceholders);
                setTimeForPlaceholder(placeholder, timeGenerator);
                counterForPlaceholders++;
            } else {
                LogSpec logSpec = logSpecs.get(counterForLogSpecs);
                Log fixedLog = setTimeForLogSpec(logSpec, timeGenerator);
                fixedLogs.add(fixedLog);
                counterForLogSpecs++;
            }
        }

        return groupFixture
                .setFixedLogs(fixedLogs)
                .setPlaceholders(placeholders);
    }

    private int computeLogCount() {
        switch (timePeriod.getType()) {
            case ANY:
                return computeApproxLogCount();
            case CUSTOM:
                return computeExactLogCount();
            default:
                throw new AssertionError();
        }
    }

    /**
     * Computes the approximate number of logs for the group.
     * The log count for the group consists of the number of log specifications
     * and a recommend number of logs that the placeholders of type {@code FLEXIBLE}
     * represent.
     * The latter is a recommendation since not enough information is available
     * to compute an exact number.
     *
     * @return The recommended log count for the group
     */
    private int computeApproxLogCount() {
        LocalTime startTime = timePeriod.getStartTime();
        LocalTime endTime = timePeriod.getEndTime();
        int seconds = endTime.toSecondOfDay() - startTime.toSecondOfDay();
        return logSpecs.size() + 1 + seconds / (AbstractTimeGenerator.SECONDS_IN_HOUR - AbstractTimeGenerator.SECONDS_IN_MINUTE);
    }

    /**
     * Computes the number of logs in the group.
     * The log count for the group consists of the number of log specifications
     * and the number of logs that the placeholders represent.
     *
     * @return The number of logs in the group
     */
    private int computeExactLogCount() {
        return logSpecs.size() +
                placeholdersWithoutPadding().stream()
                        .mapToInt(Placeholder.Builder::getLogCount)
                        .sum();
    }

    /**
     * Returns the placeholders of the group without the first and
     * last placeholders.
     *
     * @return The placeholders without padding
     */
    private List<Placeholder.Builder> placeholdersWithoutPadding() {
        return placeholders.subList(1, placeholders.size() - 2);
    }

    /**
     * Checks if an entity at {@code index} is a placeholder or a
     * log specification.
     * Placeholders are at even indices while log specifications
     * are at odd indices.
     *
     * @param index The index of the entity
     * @return True if the entity at {@code index} is a placeholder,
     *   false otherwise
     */
    private boolean isPlaceholder(int index) {
        return index % 2 == 0;
    }

    /**
     * Sets the earliest and latest time for {@code placeholder} using time
     * values generated from {@code timeGenerator}.
     * Given that a placeholder represents a number of logs, and we only require
     * the time for the first and last logs, all time values generated by
     * {@code timeGenerator} save for the first and last are discarded.
     *
     * @param placeholder The placeholder which time requires setting
     * @param timeGenerator The time generator to generate the time values
     */
    private void setTimeForPlaceholder(
            Placeholder.Builder placeholder,
            FixedBoundedTimeGenerator timeGenerator
    ) {
        LocalTime startTime = timeGenerator.generate();
        int skipCount = computeSkipCount(placeholder);
        timeGenerator.skip(skipCount);
        LocalTime endTime = timeGenerator.generate();
        placeholder.withStartTime(startTime).withEndTime(endTime);
    }

    /**
     * Computes the number of times a time generator should skip ahead.
     *
     * @param placeholder The placeholder which time period requires setting
     * @return The skip count of the time generator
     */
    private int computeSkipCount(Placeholder.Builder placeholder) {
        return placeholder.getLogCount() - TIME_SKIP_OFFSET;
    }

    /**
     * Computes the time value for {@code logSpec} and constructs a log
     * from the time generated and {@code logSpec}.
     *
     * @param logSpec The log specification
     * @param timeGenerator The time generator to generate the time value
     * @return A log constructed from {@code logSpec} and the time generated
     */
    private Log setTimeForLogSpec(LogSpec logSpec, FixedBoundedTimeGenerator timeGenerator) {
        LocalTime time = timeGenerator.generate();
        return new Log(time, logSpec);
    }
}
