package logen.experimental.generation.fluid;

import logen.experimental.generation.fixed.Fixture;
import logen.experimental.generation.fixed.Placeholder;
import logen.experimental.log.Log;
import logen.experimental.scenario.LogSpec;
import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.TimePeriod;
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

        List<List<Log>> fluidLogs = new ArrayList<>();
        List<Placeholder> placeholders = fixture.getPlaceholders();
        for (int i = 0; i < placeholders.size(); i++) {
            Placeholder placeholder = placeholders.get(i);
            TimePeriod timePeriod = placeholder.getTimePeriod();
            int logCount = placeholder.getSpacingAmount();
            List<Log> fluidLogsForPlaceholder = new ArrayList<>(logCount);

            if (i == 0) {
                LocalTime endTime = timePeriod.getEndTime();
                TimeGenerator timeGenerator = TimeGenerator.back(endTime);

                for (int j = logCount - 1; j > -1; j--) {
                    Log fluidLog = generateFluidLogForPlaceholder(logSpecPool, subjectPool, timeGenerator);
                    fluidLogsForPlaceholder.add(fluidLog);
                }
            } else if (i == placeholders.size() - 1) {
                LocalTime startTime = timePeriod.getStartTime();
                TimeGenerator timeGenerator = TimeGenerator.forward(startTime);

                for (int j = 0; j < logCount; j++) {
                    Log fluidLog = generateFluidLogForPlaceholder(logSpecPool, subjectPool, timeGenerator);
                    fluidLogsForPlaceholder.add(fluidLog);
                }
            } else {
                TimeGenerator timeGenerator = TimeGenerator.bounded(
                        timePeriod.getStartTime(),
                        timePeriod.getEndTime(),
                        logCount
                );

                for (int j = 0; j < logCount; j++) {
                    Log fluidLog = generateFluidLogForPlaceholder(logSpecPool, subjectPool, timeGenerator);
                    fluidLogsForPlaceholder.add(fluidLog);
                }
            }
            fluidLogs.add(fluidLogsForPlaceholder);
        }

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

    private Log generateFluidLogForPlaceholder(LogSpecPool logSpecPool, Pool<String> subjectPool, TimeGenerator timeGenerator) {
        LocalTime time = timeGenerator.generate();
        LogSpec logSpec = logSpecPool.get();
        String subject = subjectPool.get();
        logSpec.setSubjectIfAbsent(subject);
        return new Log(time, logSpec);
    }
}
