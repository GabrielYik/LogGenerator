package logen.generation.fixed.appliers;

import logen.generation.fixed.GroupFixture;
import logen.generation.fixed.Placeholder;
import logen.generation.fixed.PlaceholderType;
import logen.log.Log;
import logen.scenario.common.LogSpec;
import logen.scenario.group.GroupTimePeriod;
import logen.util.RandomUtil;
import logen.util.timegenerators.AbstractTimeGenerator;
import logen.util.timegenerators.BoundedTimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An applier that acts on a group by applying the time period attribute
 * defined on the group onto the logs in the group.
 */
    /**
     * An arbitrary offset based on heuristics to ensure that the
     * non-padding placeholders of the group represent at least one log.
     */
    private static final int ARBITRARY_OFFSET = 1;

    private final GroupTimePeriod timePeriod;

    /**
     * The log specifications bundled with the group fixture.
     */
    private final List<LogSpec> logSpecs;
    /**
     * The placeholders, both padding and non-padding, bundled with the
     * group fixture.
     */
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
        setLogCountForNonPaddingPlaceholders();
        int logCount = computeTotalLogCount();
        BoundedTimeGenerator timeGenerator = BoundedTimeGenerator.linear(
                timePeriod.getStartTime(),
                timePeriod.getEndTime(),
                logCount
        );

        List<Log> fixedLogs = constructFixedLogs(timeGenerator);
        setTimeForPlaceholders(fixedLogs);

        return groupFixture
                .setFixedLogs(fixedLogs)
                .setPlaceholders(placeholders);
    }

    /**
     * Sets the log count for non-padding placeholders of type {@code FLEXIBLE}.
     *
     * The log count is computed based on heuristics since not enough
     * information is available to compute an exact number.
     * If the log count is less than the number of non-padding placeholders of
     * type {@code FLEXIBLE}, some placeholders would have a log count of 0.
     */
    private void setLogCountForNonPaddingPlaceholders() {
        List<Placeholder.Builder> flexiblePlaceholders = placeholdersWithoutPadding().stream()
                .filter(placeholder -> placeholder.getType().equals(PlaceholderType.FLEXIBLE))
                .collect(Collectors.toList());
        int totalLogCount = computeTotalLogCountForPlaceholders();
        List<Integer> logCounts = RandomUtil.distributeRandomly(totalLogCount, flexiblePlaceholders.size());
        for (int i = 0; i < logCounts.size(); i++) {
            flexiblePlaceholders.get(i).withLogCount(logCounts.get(i));
        }
    }

    /**
     * Returns non-padding placeholders by removing the padding placeholders.
     *
     * @return Non-padding placeholders.
     */
    private List<Placeholder.Builder> placeholdersWithoutPadding() {
        return placeholders.subList(1, placeholders.size() - 1);
    }

    /**
     * Computes the approximate number of logs based on heuristics for non-padding
     * placeholders of type {@code FLEXIBLE}.
     * The number is only an approximation since not enough information is
     * available to compute an exact number.
     *
     * @return An approximate log count for the non-padding placeholders of type
     *   {@code FLEXIBLE}
     */
    private int computeTotalLogCountForPlaceholders() {
        LocalTime startTime = timePeriod.getStartTime();
        LocalTime endTime = timePeriod.getEndTime();
        int seconds = endTime.toSecondOfDay() - startTime.toSecondOfDay();
        return ARBITRARY_OFFSET + seconds / AbstractTimeGenerator.computeAverageInterval();
    }

    /**
     * Computes the total number of logs in the group.
     * The log count for the group consists of the number of log specifications
     * and the number of logs that the non-padding placeholders represent.
     *
     * @return The total number of logs in the group
     */
    private int computeTotalLogCount() {
        return logSpecs.size() +
                placeholdersWithoutPadding().stream()
                        .mapToInt(Placeholder.Builder::getLogCount)
                        .sum();
    }

    private List<Log> constructFixedLogs(BoundedTimeGenerator timeGenerator) {
        List<Log> fixedLogs = new ArrayList<>();
        for (int i = 0; i < logSpecs.size(); i++) {
            LogSpec logSpec = logSpecs.get(i);
            LocalTime time = timeGenerator.generate();
            Log fixedLog = new Log(time, logSpec);
            fixedLogs.add(fixedLog);
            if (isNonPaddingPlaceholderNext(i)) {
                timeGenerator.skip(placeholders.get(i).getLogCount());
            }
        }
        return fixedLogs;
    }

    /**
     * Checks if the placeholder at index {@code i} is non-padding.
     *
     * @param i The index of the placeholder
     * @return True if the placeholder is non-padding, false otherwise
     */
    private boolean isNonPaddingPlaceholderNext(int i) {
        return i >= 1 && i <= placeholders.size() - 2;
    }

    private void setTimeForPlaceholders(List<Log> fixedLogs) {
        setTimeForPaddingPlaceholders(fixedLogs);
        setTimeForNonPaddingPlaceholders(fixedLogs);
    }

    private void setTimeForPaddingPlaceholders(List<Log> fixedLogs) {
        placeholders.get(0)
                .withStartTime(null)
                .withEndTime(fixedLogs.get(0).getTime());
        placeholders.get(placeholders.size() - 1)
                .withStartTime(fixedLogs.get(fixedLogs.size() - 1).getTime())
                .withEndTime(null);
    }

    private void setTimeForNonPaddingPlaceholders(List<Log> fixedLogs) {
        for (int i = 1; i < placeholders.size() - 1; i++) {
            Log leftFixedLog = fixedLogs.get(i - 1);
            Log rightFixedLog = fixedLogs.get(i);
            placeholders.get(i)
                    .withStartTime(leftFixedLog.getTime())
                    .withEndTime(rightFixedLog.getTime());
        }
    }
}
