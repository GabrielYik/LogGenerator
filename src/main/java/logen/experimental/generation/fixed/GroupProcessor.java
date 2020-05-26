package logen.experimental.generation.fixed;

import javafx.util.Pair;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.group.Group;
import logen.experimental.scenario.group.GroupSpacing;
import logen.experimental.scenario.group.GroupTimePeriod;
import logen.experimental.util.TimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A processor that applies the scenario group attributes that define
 * fixed logs on the logs in a group.
 */
public class GroupProcessor {
    private static final int ONE_TO_ZERO_BASED_INDEX_OFFSET = 1;
    private static final int LOG_SPEC_TO_PLACEHOLDER_SIZE_OFFSET = 1;
    /**
     * One time skip since the earliest time has already been generated
     * and one time skip since the latest time has yet to be generated.
     */
    private static final int TIME_SKIP_OFFSET = 2;
    /**
     * One placeholder for the space before the first fixed log of a group
     * and another for the space after the last fixed log of a group.
     */
    private static final int EXTERNAL_PLACEHOLDER_COUNT = 2;

    private final Group group;

    /**
     * Constructs a group processor from {@code Group}.
     * @param group The group to process
     */
    public GroupProcessor(Group group) {
        this.group = group;
    }

    /**
     * Applies the scenario group attributes:
     * <ul>
     *     <li>ordering
     *     <li>spacing
     *     <li>time period
     * </ul>
     * to the logs in the group
     * @return a group fixture that bundles both the fixed logs and
     * incomplete placeholders generated from the group
     */
    public GroupFixture process() {
        List<LogSpec> logSpecs = applyOrdering();
        List<Placeholder.Builder> placeholders = applySpacing();
        Pair<List<Log>, List<Placeholder.Builder>> result = applyTimePeriod(
                logSpecs,
                placeholders
        );
        result = applyFrequency(result.getKey(), result.getValue());
        return new GroupFixture(result.getKey(), result.getValue());
    }

    private List<LogSpec> applyOrdering() {
        List<Integer> sequence = group.getOrdering().getSequence();
        List<LogSpec> logSpecs = group.getLogSpecs();
        return orderLogSpecs(sequence, logSpecs);
    }

    private List<LogSpec> orderLogSpecs(List<Integer> sequence, List<LogSpec> logSpecs) {
        List<LogSpec> orderedLogSpecs = new ArrayList<>(logSpecs.size());
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i) - ONE_TO_ZERO_BASED_INDEX_OFFSET;
            LogSpec logSpec = logSpecs.get(i);
            orderedLogSpecs.add(index, logSpec);
        }
        return orderedLogSpecs;
    }

    private List<Placeholder.Builder> applySpacing() {
        int logCount = group.getLogSpecs().size();
        int placeholderCount = logCount - LOG_SPEC_TO_PLACEHOLDER_SIZE_OFFSET + EXTERNAL_PLACEHOLDER_COUNT;
        GroupSpacing spacing = group.getSpacing();
        return generatePlaceholders(placeholderCount, spacing);
    }

    private List<Placeholder.Builder> generatePlaceholders(int placeholderCount, GroupSpacing spacing) {
        List<Placeholder.Builder> placeholders = new ArrayList<>(placeholderCount);
        Placeholder.Builder firstPlaceholder = new Placeholder.Builder()
                .withType(PlaceholderType.FLEXIBLE);
        Placeholder.Builder lastPlaceholder = new Placeholder.Builder()
                .withType(PlaceholderType.FLEXIBLE);
        placeholders.add(0, firstPlaceholder);
        placeholders.add(placeholders.size() - 1, lastPlaceholder);

        switch(spacing.getType()) {
            case ANY:
                for (int i = 1; i < placeholders.size() - 1; i++) {
                    Placeholder.Builder placeholder = new Placeholder.Builder()
                            .withType(PlaceholderType.FLEXIBLE);
                    placeholders.add(placeholder);
                }
                break;
            case CUSTOM:
                List<Integer> spaceAmount = spacing.getAmount();
                for (int i = 1; i < spaceAmount.size() - 1; i++) {
                    Placeholder.Builder placeholder = new Placeholder.Builder()
                            .withType(PlaceholderType.CUSTOM)
                            .withLogCount(spaceAmount.get(i));
                    placeholders.add(placeholder);
                }
                break;
            default:
                throw new AssertionError();
        }
        return placeholders;
    }

    private Pair<List<Log>, List<Placeholder.Builder>> applyTimePeriod(
            List<LogSpec> orderedLogSpecs,
            List<Placeholder.Builder> placeholders
    ) {
        GroupTimePeriod timePeriod = group.getTimePeriod();
        int approximateLogCount = computeApproxLogCount(orderedLogSpecs.size(), placeholders);
        TimeGenerator timeGenerator = TimeGenerator.bounded(
                timePeriod.getStartTime(),
                timePeriod.getEndTime(),
                approximateLogCount
        );

        List<Log> fixedLogs = new ArrayList<>();
        int counter = orderedLogSpecs.size() + placeholders.size();
        int counterForLogSpecs = 0;
        int counterForPlaceholders = 0;
        for (int i = 0; i < counter; i++) {
            if (i % 2 == 0) {
                Placeholder.Builder placeholder = placeholders.get(counterForPlaceholders);

                LocalTime startTime = timeGenerator.generate();
                timeGenerator.skip(placeholder.getLogCount() - TIME_SKIP_OFFSET);
                LocalTime endTime = timeGenerator.generate();

                placeholder.withStartTime(startTime).withEndTime(endTime);
                counterForPlaceholders++;
            } else {
                LocalTime time = timeGenerator.generate();
                LogSpec logSpec = orderedLogSpecs.get(counterForLogSpecs);
                Log fixedLog = new Log(time, logSpec);
                fixedLogs.add(fixedLog);
                counterForLogSpecs++;
            }
        }
        return new Pair<>(fixedLogs, placeholders);
    }

    private int computeApproxLogCount(
            int logCount,
            List<Placeholder.Builder> placeholders
    ) {
        return logCount + placeholders.stream()
                .mapToInt(Placeholder.Builder::getLogCount).sum();
    }

    private Pair<List<Log>, List<Placeholder.Builder>> applyFrequency(
            List<Log> fixedLogs,
            List<Placeholder.Builder> placeholders
    ) {
        int count = group.getFrequency().getCount();
        if (count == 1) {
            return new Pair<>(fixedLogs, placeholders);
        } else {
            List<Log> multipliedFixedLogs = multipleLogs(fixedLogs, count);
            List<Placeholder.Builder> multipliedPlaceholders = multipleAndOverlapPlaceholders(
                    placeholders, count
            );
            return new Pair<>(multipliedFixedLogs, multipliedPlaceholders);
        }
    }

    private List<Log> multipleLogs(List<Log> fixedLogs, int count) {
        List<Log> consolidatedFixedLogs = new ArrayList<>();
        int counter = 0;
        do {
            consolidatedFixedLogs.addAll(fixedLogs);
            counter++;
        } while (counter < count);
        return consolidatedFixedLogs;
    }

    private List<Placeholder.Builder> multipleAndOverlapPlaceholders(
            List<Placeholder.Builder> placeholders,
            int count
    ) {
        List<Placeholder.Builder> consolidatedPlaceholders = new ArrayList<>(placeholders);
        int counter = 1;
        do {
            int lastIndex = consolidatedPlaceholders.size() - 1;
            consolidatedPlaceholders.remove(lastIndex);
            consolidatedPlaceholders.addAll(placeholders);
            counter++;
        } while (counter < count);
        return consolidatedPlaceholders;
    }
}
