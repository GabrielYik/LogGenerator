package logen.experimental.generation.fixed;

import logen.experimental.log.Log;
import logen.experimental.scenario.LogSpec;
import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.Group;
import logen.experimental.scenario.TimePeriod;
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
        List<Placeholder.Builder> placeholders = applySpacing(group.getSpacings());
        return applyTimePeriod(group.getTimePeriod(), orderedLogSpecs, placeholders);
    }

    private List<LogSpec> applyOrder(List<Integer> order, List<LogSpec> logSpecs) {
        List<LogSpec> orderedLogSpecs = new ArrayList<>(logSpecs.size());
        for (int i = 0; i < order.size(); i++) {
            int index = order.get(i) - 1;
            LogSpec logSpec = logSpecs.get(i);
            orderedLogSpecs.add(index, logSpec);
        }
        return orderedLogSpecs;
    }

    private List<Placeholder.Builder> applySpacing(List<String> spacings) {
        List<Placeholder.Builder> placeholders = new ArrayList<>(spacings.size());
        Placeholder.Builder externalPlaceholder = new Placeholder.Builder().withSpacing("any");
        placeholders.add(externalPlaceholder);
        for (String spacing : spacings) {
            Placeholder.Builder placeholder = new Placeholder.Builder().withSpacing(spacing);
            placeholders.add(placeholder);
        }
        placeholders.add(externalPlaceholder);
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
            if (i % 2 != 0) {
                LocalTime time = timeGenerator.generate();
                LogSpec logSpec = orderedLogSpecs.get(logSpecsCounter);
                Log fixedLog = new Log(time, logSpec);
                fixedLogs.add(fixedLog);
                logSpecsCounter++;
            } else {
                Placeholder.Builder placeholder = placeholders.get(placeholdersCounter);
                LocalTime startTime = timeGenerator.generate();
                timeGenerator.skip(placeholder.getSpacingAmount() - 2);
                LocalTime endTime = timeGenerator.generate();
                placeholder.withTimePeriod(new TimePeriod(startTime, endTime));
                placeholdersCounter++;
            }
        }
        return new Fixture(
                fixedLogs,
                placeholders.stream().map(Placeholder.Builder::build).collect(Collectors.toList())
        );
    }

    private int computeApproximateLogCount(int orderedLogSpecCount, List<Placeholder.Builder> placeholders) {
        return orderedLogSpecCount + placeholders.stream().mapToInt(Placeholder.Builder::getSpacingAmount).sum();
    }
}
