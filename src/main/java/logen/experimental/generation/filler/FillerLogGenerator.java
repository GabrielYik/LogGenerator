package logen.experimental.generation.filler;

import logen.experimental.generation.fixed.Fixture;
import logen.experimental.generation.fixed.Placeholder;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.Scenario;
import logen.experimental.util.Pool;
import logen.experimental.util.timegenerators.BoundedTimeGenerator;
import logen.experimental.util.timegenerators.TimeGenerator;
import logen.experimental.util.timegenerators.UnboundedTimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A generator of filler logs.
 * The generator has to be executed after the generation of fixed logs.
 */
public class FillerLogGenerator {
    private final Scenario scenario;
    private final Fixture fixture;

    public FillerLogGenerator(Scenario scenario, Fixture fixture) {
        this.scenario = scenario;
        this.fixture = fixture;
    }

    /**
     * Generates filler logs and consolidates both the fixed logs
     * from the fixture from a {@code FixedLogGenerator} and the
     * filler logs generated.
     * @return A consolidated list of fixed and filler logs.
     */
    public List<Log> generate() {
        Pool<LogSpec> logSpecPool = new Pool<>(
                scenario.getLogSpecs(),
                LogSpec::decrementFrequency,
                LogSpec::isExhausted
        );
        Pool<String> subjectPool = new Pool<>(
                scenario.getSubjects(),
                s -> {},
                s -> false
        );
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

        List<List<Log>> fillerLogsForBetweenPlaceholders = generateLogsForBetweenPlaceholders(
                logSpecPool,
                subjectPool,
                placeholders
        );

        List<List<Log>> fluidLogs = new ArrayList<>();
        fluidLogs.add(fluidLogsForFirstPlaceholder);
        fluidLogs.addAll(fillerLogsForBetweenPlaceholders);
        fluidLogs.add(fluidLogsForLastPlaceholder);

        List<Log> fixedLogs = fixture.getFixedLogs();
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

    private List<Log> generateLogsForFirstPlaceholder(
            Pool<LogSpec> logSpecPool,
            Pool<String> subjectPool,
            Placeholder placeholder
    ) {
        TimeGenerator timeGenerator = UnboundedTimeGenerator.backward(
                placeholder.getEndTime(),
                scenario.getTimePeriod().getStartTime(),
                scenario.getTimePeriod().getEndTime()
        );
        List<Log> fluidLogs = new ArrayList<>();
        for (int i = placeholder.getLogCount() - 1; i > -1; i--) {
            Log fluidLog = generateLog(logSpecPool, subjectPool, timeGenerator);
            fluidLogs.add(fluidLog);
        }
        return fluidLogs;
    }

    private List<Log> generateLogsForLastPlaceholder(
            Pool<LogSpec> logSpecPool,
            Pool<String> subjectPool,
            Placeholder placeholder
    ) {
        TimeGenerator timeGenerator = UnboundedTimeGenerator.forward(
                placeholder.getStartTime(),
                scenario.getTimePeriod().getEndTime(),
                scenario.getTimePeriod().getStartTime()
        );
        List<Log> fluidLogs = new ArrayList<>();
        for (int i = 0; i < placeholder.getLogCount(); i++) {
            Log fluidLog = generateLog(logSpecPool, subjectPool, timeGenerator);
            fluidLogs.add(fluidLog);
        }
        return fluidLogs;
    }

    private List<List<Log>> generateLogsForBetweenPlaceholders(
            Pool<LogSpec> logSpecPool,
            Pool<String> subjectPool,
            List<Placeholder> placeholders
    ) {
        List<List<Log>> fillerLogs = new ArrayList<>();
        for (int i = 1; i < placeholders.size() - 1; i++) {
            Placeholder placeholder = placeholders.get(i);

            switch(placeholder.getType()) {
                case FLEXIBLE:
                    break;
                case CUSTOM:
                    int logCount = placeholder.getLogCount();
                    TimeGenerator timeGenerator = BoundedTimeGenerator.wrap(
                            placeholder.getStartTime(),
                            placeholder.getEndTime(),
                            scenario.getTimePeriod().getStartTime(),
                            scenario.getTimePeriod().getEndTime(),
                            logCount
                    );

                    List<Log> fillerLogsForPlaceholder = new ArrayList<>(logCount);
                    for (int j = 0; j < logCount; j++) {
                        Log fillerLog = generateLog(logSpecPool, subjectPool, timeGenerator);
                        fillerLogsForPlaceholder.add(fillerLog);
                    }
                    fillerLogs.add(fillerLogsForPlaceholder);
            }
        }
        return fillerLogs;
    }

    private Log generateLog(
            Pool<LogSpec> logSpecPool,
            Pool<String> subjectPool,
            TimeGenerator timeGenerator
    ) {
        LocalTime time = timeGenerator.generate();
        LogSpec logSpec = logSpecPool.get();
        String subject = subjectPool.get();
        logSpec.setSubjectIfAbsent(subject);
        return new Log(time, logSpec);
    }
}
