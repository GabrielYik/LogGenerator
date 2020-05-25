package logen.experimental.generation.fixed;

import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.group.Group;
import logen.experimental.scenario.group.Order;
import logen.experimental.scenario.group.Space;
import logen.experimental.scenario.group.SpaceType;
import logen.experimental.scenario.time.TimePeriod;
import logen.experimental.util.TimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FixedLogGenerator {
    private static final int EXTERNAL_PLACEHOLDER_COUNT = 2;

    private final Scenario scenario;

    public FixedLogGenerator(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Need to settle order, spacing and time period.
     * Assume one group first.
     */
    public Fixture generate() {
        Group group = scenario.getGroups().get(0);
        List<LogSpec> orderedLogSpecs = applyOrder(group.getOrder(), group.getLogSpecs());
        List<Placeholder.Builder> placeholders = applySpacing(
                group.getLogSpecs().size(),
                group.getSpace()
        );
        return applyTimePeriod(group.getTimePeriod(), orderedLogSpecs, placeholders);
    }

    private List<LogSpec> applyOrder(Order order, List<LogSpec> logSpecs) {
        List<LogSpec> orderedLogSpecs = new ArrayList<>(logSpecs.size());
        List<Integer> sequence = order.getSequence();
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i) - 1;
            LogSpec logSpec = logSpecs.get(i);
            orderedLogSpecs.add(index, logSpec);
        }
        return orderedLogSpecs;
    }

    private List<Placeholder.Builder> applySpacing(int logCount, Space space) {
        List<Placeholder.Builder> placeholders = new ArrayList<>(logCount - 1 + EXTERNAL_PLACEHOLDER_COUNT);
        Placeholder.Builder externalPlaceholder = new Placeholder.Builder()
                .withSpaceType(SpaceType.ANY)
                .withSpaceAmount(Space.AMOUNT_ANY);
        placeholders.add(0, externalPlaceholder);
        placeholders.add(placeholders.size() - 1, externalPlaceholder);

        switch(space.getType()) {
            case ANY:
                for (int i = 1; i < placeholders.size() - 1; i++) {
                    Placeholder.Builder placeholder = new Placeholder.Builder()
                            .withSpaceType(SpaceType.ANY)
                            .withSpaceAmount(Space.AMOUNT_ANY);
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

    private Fixture applyTimePeriod(
            TimePeriod timePeriod,
            List<LogSpec> orderedLogSpecs,
            List<Placeholder.Builder> placeholders
    ) {
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
        return new Fixture(
                fixedLogs,
                placeholders.stream().map(Placeholder.Builder::build).collect(Collectors.toList())
        );
    }

    private int computeApproximateLogCount(int orderedLogSpecCount, List<Placeholder.Builder> placeholders) {
        return orderedLogSpecCount + placeholders.stream().mapToInt(Placeholder.Builder::getSpaceAmount).sum();
    }
}
