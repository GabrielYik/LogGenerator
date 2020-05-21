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
        List<Placeholder> placeholders = applySpacing(group.getSpacings());
        List<Log> fixedLogs = applyTimePeriod(group.getTimePeriod(), orderedLogSpecs, placeholders);
        return new Fixture(fixedLogs, placeholders);
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

    private List<Placeholder> applySpacing(List<String> spacings) {
        List<Placeholder> placeholders = new ArrayList<>(spacings.size() + EXTERNAL_PLACEHOLDER_COUNT);
        //TODO use enum or constant
        Placeholder externalPlaceholder = new Placeholder("any");
        placeholders.add(externalPlaceholder);
        for (String spacing : spacings) {
            Placeholder placeholder = new Placeholder(spacing);
            placeholders.add(placeholder);
        }
        placeholders.add(externalPlaceholder);
        return placeholders;
    }

    private List<Log> applyTimePeriod(
            TimePeriod timePeriod,
            List<LogSpec> orderedLogSpecs,
            List<Placeholder> placeholders
    ) {
        int approximateLogCount = computeApproximateLogCount(orderedLogSpecs.size(), placeholders);
        TimeGenerator timeGenerator = new TimeGenerator(timePeriod, approximateLogCount);
        List<Log> fixedLogs = new ArrayList<>();
        for (LogSpec logSpec : orderedLogSpecs) {
            LocalTime time = timeGenerator.generate();
            Log fixedLog = new Log(time, logSpec);
            fixedLogs.add(fixedLog);
        }
        return fixedLogs;
    }

    private int computeApproximateLogCount(int orderedLogSpecCount, List<Placeholder> placeholders) {
        return orderedLogSpecCount + placeholders.stream().mapToInt(Placeholder::getSpacingAmount).sum();
    }
}
