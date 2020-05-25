package logen.experimental.generation.fixed;

import javafx.util.Pair;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.group.Group;
import logen.experimental.scenario.group.Order;
import logen.experimental.scenario.group.Space;
import logen.experimental.scenario.group.SpaceType;
import logen.experimental.scenario.time.TimePeriod;
import logen.experimental.util.TimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GroupProcessor {
    private static final int EXTERNAL_PLACEHOLDER_COUNT = 2;

    private final Group group;

    public GroupProcessor(Group group) {
        this.group = group;
    }

    public GroupFixture process() {
        List<LogSpec> logSpecs = applyOrder();
        List<Placeholder.Builder> placeholders = applySpacing();
        Pair<List<Log>, List<Placeholder.Builder>> result = applyTimePeriod(
                logSpecs,
                placeholders
        );
        return new GroupFixture(result.getKey(), result.getValue());
    }

    private List<LogSpec> applyOrder() {
        Order order = group.getOrder();
        List<LogSpec> logSpecs = group.getLogSpecs();

        List<LogSpec> orderedLogSpecs = new ArrayList<>(logSpecs.size());
        List<Integer> sequence = order.getSequence();
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i) - 1;
            LogSpec logSpec = logSpecs.get(i);
            orderedLogSpecs.add(index, logSpec);
        }
        return orderedLogSpecs;
    }

    private List<Placeholder.Builder> applySpacing() {
        int logCount = group.getLogSpecs().size();
        Space space = group.getSpace();

        List<Placeholder.Builder> placeholders = new ArrayList<>(logCount - 1 + EXTERNAL_PLACEHOLDER_COUNT);
        Placeholder.Builder externalPlaceholder = new Placeholder.Builder()
                .withSpaceType(SpaceType.ANY);
        placeholders.add(0, externalPlaceholder);
        placeholders.add(placeholders.size() - 1, externalPlaceholder);

        switch(space.getType()) {
            case ANY:
                for (int i = 1; i < placeholders.size() - 1; i++) {
                    Placeholder.Builder placeholder = new Placeholder.Builder()
                            .withSpaceType(SpaceType.ANY);
                    placeholders.add(placeholder);
                }
                break;
            case CUSTOM:
                List<Integer> spaceAmount = space.getAmount();
                for (int i = 1; i < spaceAmount.size() - 1; i++) {
                    Placeholder.Builder placeholder = new Placeholder.Builder()
                            .withSpaceType(SpaceType.CUSTOM)
                            .withSpaceAmount(spaceAmount.get(i));
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
        TimePeriod timePeriod = group.getTimePeriod();
        int approximateLogCount = computeApproximateLogCount(orderedLogSpecs.size(), placeholders);
        TimeGenerator timeGenerator = TimeGenerator.bounded(
                timePeriod.getStartTime(),
                timePeriod.getEndTime(),
                approximateLogCount
        );

        List<Log> fixedLogs = new ArrayList<>();
        int counter = orderedLogSpecs.size() + placeholders.size();
        int logSpecsCounter = 0;
        int placeholdersCounter = 0;
        for (int i = 0; i < counter; i++) {
            if (i % 2 == 0) {
                Placeholder.Builder placeholder = placeholders.get(placeholdersCounter);
                LocalTime startTime = timeGenerator.generate();
                timeGenerator.skip(placeholder.getSpaceAmount() - 2);
                LocalTime endTime = timeGenerator.generate();
                placeholder.withTimePeriod(new TimePeriod(startTime, endTime));
                placeholdersCounter++;
            } else {
                LocalTime time = timeGenerator.generate();
                LogSpec logSpec = orderedLogSpecs.get(logSpecsCounter);
                Log fixedLog = new Log(time, logSpec);
                fixedLogs.add(fixedLog);
                logSpecsCounter++;
            }
        }
        return new Pair<>(fixedLogs, placeholders);
    }

    private int computeApproximateLogCount(int orderedLogSpecCount, List<Placeholder.Builder> placeholders) {
        return orderedLogSpecCount + placeholders.stream().mapToInt(Placeholder.Builder::getSpaceAmount).sum();
    }
}
