package logen.experimental.generation.fluid;

import logen.experimental.generation.fixed.Fixture;
import logen.experimental.generation.fixed.Placeholder;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.time.TimePeriod;
import logen.experimental.util.Pool;
import logen.experimental.util.TimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FluidLogGenerator {
    private final Scenario scenario;
    private final Fixture fixture;

    public FluidLogGenerator(Scenario scenario, Fixture fixture) {
        this.scenario = scenario;
        this.fixture = fixture;
    }

    public List<Log> generate() {
        LogSpecPool logSpecPool = new LogSpecPool(scenario.getLogSpecs());
        Pool<String> subjectPool = new Pool<>(scenario.getSubjects());
        List<Placeholder> placeholders = fixture.getPlaceholders();

        List<Log> fluidLogsForFirstPlaceholder = generateLogsForFirstPlaceholder(
                logSpecPool,
                subjectPool,
                placeholders.get(0)
        );
        List<Log> fluidLogsForLastPlaceholder = generateLogsForLastPlaceholder(
                logSpecPool,
                subjectPool,
                placeholders.get(placeholders.size() - 1)
        );

        List<List<Log>> fluidLogsForRemainingPlaceholders = generateLogsForRemainingPlaceholders(
                logSpecPool,
                subjectPool,
                placeholders
        );

        List<List<Log>> fluidLogs = new ArrayList<>();
        fluidLogs.add(fluidLogsForFirstPlaceholder);
        fluidLogs.addAll(fluidLogsForRemainingPlaceholders);
        fluidLogs.add(fluidLogsForLastPlaceholder);

        List<Log> fixedLogs = fixture.getLogs();
        int counter = fixedLogs.size() + fluidLogs.size();
        int counterForFixedLogs = 0;
        int counterForFluidLogs = 0;
        List<Log> logs = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            if (i % 2 == 0) {
                logs.addAll(fluidLogs.get(counterForFluidLogs));
                counterForFluidLogs++;
            } else {
                logs.add(fixedLogs.get(counterForFixedLogs));
                counterForFixedLogs++;
            }
        }
        return logs;
    }

    private List<Log> generateLogsForFirstPlaceholder(LogSpecPool logSpecPool, Pool<String> subjectPool, Placeholder placeholder) {
        TimeGenerator timeGenerator = TimeGenerator.back(
                scenario.getTimePeriod().getStartTime(),
                placeholder.getTimePeriod().getEndTime()
        );
        List<Log> fluidLogs = new ArrayList<>();
        for (int i = placeholder.getSpacingAmount() - 1; i > -1; i--) {
            Log fluidLog = generateLog(logSpecPool, subjectPool, timeGenerator);
            fluidLogs.add(fluidLog);
        }
        return fluidLogs;
    }

    private List<Log> generateLogsForLastPlaceholder(LogSpecPool logSpecPool, Pool<String> subjectPool, Placeholder placeholder) {
        TimeGenerator timeGenerator = TimeGenerator.forward(
                placeholder.getTimePeriod().getStartTime(),
                scenario.getTimePeriod().getEndTime()
        );
        List<Log> fluidLogs = new ArrayList<>();
        for (int i = 0; i < placeholder.getSpacingAmount(); i++) {
            Log fluidLog = generateLog(logSpecPool, subjectPool, timeGenerator);
            fluidLogs.add(fluidLog);
        }
        return fluidLogs;
    }

    private List<List<Log>> generateLogsForRemainingPlaceholders(LogSpecPool logSpecPool, Pool<String> subjectPool, List<Placeholder> placeholders) {
        List<List<Log>> fluidLogs = new ArrayList<>();
        for (int i = 1; i < placeholders.size() - 1; i++) {
            Placeholder placeholder = placeholders.get(i);
            TimePeriod timePeriod = placeholder.getTimePeriod();
            int logCount = placeholder.getSpacingAmount();

            TimeGenerator timeGenerator = TimeGenerator.bounded(
                    timePeriod.getStartTime(),
                    timePeriod.getEndTime(),
                    logCount
            );

            List<Log> fluidLogsForPlaceholder = new ArrayList<>(logCount);
            for (int j = 0; j < logCount; j++) {
                Log fluidLog = generateLog(logSpecPool, subjectPool, timeGenerator);
                fluidLogsForPlaceholder.add(fluidLog);
            }

            fluidLogs.add(fluidLogsForPlaceholder);
        }
        return fluidLogs;
    }

    private Log generateLog(LogSpecPool logSpecPool, Pool<String> subjectPool, TimeGenerator timeGenerator) {
        LocalTime time = timeGenerator.generate();
        LogSpec logSpec = logSpecPool.get();
        String subject = subjectPool.get();
        logSpec.setSubjectIfAbsent(subject);
        return new Log(time, logSpec);
    }
}
