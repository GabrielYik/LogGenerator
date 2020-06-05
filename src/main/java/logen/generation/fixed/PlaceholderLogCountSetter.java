package logen.generation.fixed;

import logen.generation.common.Placeholder;
import logen.generation.common.PlaceholderType;
import logen.scenario.Scenario;
import logen.util.RandomUtil;
import logen.util.timegenerators.BoundedTimeGenerator;
import logen.util.timegenerators.TimeGenerator;

import java.time.LocalTime;
import java.util.List;

/**
 * An allocator that allocates a number of logs to placeholders of type
 * {@code FLEXIBLE}.
 * <p>
 * The preprocessor has to be executed before the generation of filler logs.
 */
public class PlaceholderLogCountSetter {
    private final Scenario scenario;
    private final GroupFixture groupFixture;
    private final List<Placeholder.Builder> placeholders;

    /**
     * Constructs a placeholder log count setter from {@code scenario}
     * and {@code groupFixture}.
     *
     * @param scenario A scenario
     * @param groupFixture A group fixture
     */
    public PlaceholderLogCountSetter(
            Scenario scenario,
            GroupFixture groupFixture
    ) {
        this.scenario = scenario;
        this.groupFixture = groupFixture;
        placeholders = groupFixture.getPlaceholders();
    }
    /**
     * Sets the number of logs that a placeholder of type {@code FLEXIBLE}
     * specifies.
     * If the placeholder is not of type {@code FLEXIBLE}, no change will
     * be made to the placeholder.
     *
     * @return The group fixture with all placeholders having their log
     *   count set
     */
    public GroupFixture set() {
        setLogCountForInnerPlaceholders();
        setLogCountForOuterPlaceholders();
        return groupFixture;
    }

    private void setLogCountForInnerPlaceholders() {
        for (int i = 1; i < placeholders.size() - 1; i++) {
            Placeholder.Builder placeholder = placeholders.get(i);
            if (!placeholder.getType().equals(PlaceholderType.FLEXIBLE)) {
                continue;
            }

            TimeGenerator timeGenerator = BoundedTimeGenerator.between(
                    placeholder.getStartTime(),
                    placeholder.getEndTime(),
                    scenario.getTimePeriod().getEndTime(),
                    scenario.getTimePeriod().getStartTime()
            );
            int logCount = computeLogCountForInnerPlaceholder(timeGenerator);
            placeholder.withLogCount(logCount);
        }
    }

    private int computeLogCountForInnerPlaceholder(TimeGenerator timeGenerator) {
        int logCount = 0;
        LocalTime time = timeGenerator.generate();
        while (time != null) {
            logCount++;
            time = timeGenerator.generate();
        }
        return logCount;
    }

    private void setLogCountForOuterPlaceholders() {
        int logCount = computeLogCountForOuterPlaceholders();
        distributeOverOuterPlaceholders(logCount);
    }

    private int computeLogCountForOuterPlaceholders() {
        int logCount = computeRemainingLogCount();
        if (hasNoneLeft(logCount)) {
            return scenario.getLogCount() / 3;
        } else if (hasNotEnough(logCount)) {
            return logCount / 3 + logCount;
        } else {
            return logCount;
        }
    }

    /**
     * Checks if there are any logs left.
     *
     * @param logCount The number of logs
     * @return True if there are logs left, false otherwise
     */
    private boolean hasNoneLeft(int logCount) {
        return logCount <= 0;
    }

    /**
     * Checks if the number of logs is not enough.
     *
     * @param logCount The number of logs
     * @return True if there are not enough logs left,
     *   false otherwise
     */
    private boolean hasNotEnough(int logCount) {
        int sufficiencyFloor = scenario.getLogCount() / 3;
        return logCount < sufficiencyFloor;
    }

    private int computeRemainingLogCount() {
        return scenario.getLogCount() - computeCurrentLogCount();
    }

    private int computeCurrentLogCount() {
        return groupFixture.getFixedLogs().size() + (int) placeholders.stream()
                .filter(placeholder -> placeholder.getType().equals(PlaceholderType.FLEXIBLE))
                .map(Placeholder.Builder::getLogCount)
                .count();
    }

    /**
     * Distributes {@code logCount} randomly over the two outer
     * placeholders.
     *
     * @param logCount The number of logs to distribute
     */
    private void distributeOverOuterPlaceholders(int logCount) {
        List<Integer> randomDistribution = RandomUtil.distributeRandomly(logCount, 2);
        Placeholder.Builder firstPlaceholder = placeholders.get(0);
        firstPlaceholder.withLogCount(randomDistribution.get(0));
        Placeholder.Builder lastPlaceholder = placeholders.get(placeholders.size() - 1);
        lastPlaceholder.withLogCount(randomDistribution.get(1));
    }
}
