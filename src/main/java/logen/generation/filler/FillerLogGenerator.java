package logen.generation.filler;

import logen.generation.common.Fixture;
import logen.generation.common.Placeholder;
import logen.log.Log;
import logen.scenario.common.LogSpec;
import logen.scenario.Scenario;
import logen.util.Pool;
import logen.util.timegenerators.StrictTimeGenerator;
import logen.util.timegenerators.TimeGenerator;
import logen.util.timegenerators.UnboundedTimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
        List<Placeholder> placeholders = fixture.getPlaceholders();

        List<Log> fillerLogsForFirstPlaceholder = generateLogsForFirstPlaceholder(
                logSpecPool,
                placeholders.get(0)
        );
        List<Log> fillerLogsForLastPlaceholder = generateLogsForLastPlaceholder(
                logSpecPool,
                placeholders.get(placeholders.size() - 1)
        );

        List<List<Log>> fillerLogsForBetweenPlaceholders = generateLogsForBetweenPlaceholders(
                logSpecPool,
                placeholders
        );

        List<List<Log>> fillerLogs = new ArrayList<>();
        fillerLogs.add(fillerLogsForFirstPlaceholder);
        fillerLogs.addAll(fillerLogsForBetweenPlaceholders);
        fillerLogs.add(fillerLogsForLastPlaceholder);

        List<Log> fixedLogs = fixture.getFixedLogs();
        int counter = fixedLogs.size() + fillerLogs.size();
        int counterForFixedLogs = 0;
        int counterForFillerLogs = 0;
        List<Log> logs = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            if (i % 2 == 0) {
                logs.addAll(fillerLogs.get(counterForFillerLogs));
                counterForFillerLogs++;
            } else {
                logs.add(fixedLogs.get(counterForFixedLogs));
                counterForFixedLogs++;
            }
        }
        return logs;
    }

    private List<Log> generateLogsForFirstPlaceholder(
            Pool<LogSpec> logSpecPool,
            Placeholder placeholder
    ) {
        TimeGenerator timeGenerator = UnboundedTimeGenerator.backward(
                placeholder.getEndTime(),
                scenario.getTimePeriod().getStartTime(),
                scenario.getTimePeriod().getEndTime()
        );
        List<Log> fillerLogs = new LinkedList<>();
        for (int i = placeholder.getLogCount() - 1; i > -1; i--) {
            Log fillerLog = constructFillerLog(logSpecPool, timeGenerator);
            fillerLogs.add(0, fillerLog);
        }
        return fillerLogs;
    }

    private List<Log> generateLogsForLastPlaceholder(
            Pool<LogSpec> logSpecPool,
            Placeholder placeholder
    ) {
        TimeGenerator timeGenerator = UnboundedTimeGenerator.forward(
                placeholder.getStartTime(),
                scenario.getTimePeriod().getEndTime(),
                scenario.getTimePeriod().getStartTime()
        );
        List<Log> fillerLogs = new ArrayList<>();
        for (int i = 0; i < placeholder.getLogCount(); i++) {
            Log fillerLog = constructFillerLog(logSpecPool, timeGenerator);
            fillerLogs.add(fillerLog);
        }
        return fillerLogs;
    }

    private List<List<Log>> generateLogsForBetweenPlaceholders(
            Pool<LogSpec> logSpecPool,
            List<Placeholder> placeholders
    ) {
        List<List<Log>> fillerLogs = new ArrayList<>();
        for (int i = 1; i < placeholders.size() - 1; i++) {
            Placeholder placeholder = placeholders.get(i);

            int logCount = placeholder.getLogCount();
            if (logCount == 0) {
                fillerLogs.add(Collections.emptyList());
                continue;
            }

            TimeGenerator timeGenerator = StrictTimeGenerator.wrap(
                    placeholder.getStartTime(),
                    placeholder.getEndTime(),
                    scenario.getTimePeriod().getEndTime(),
                    scenario.getTimePeriod().getStartTime(),
                    logCount
            );

            List<Log> fillerLogsForPlaceholder = new ArrayList<>(logCount);
            for (int j = 0; j < logCount; j++) {
                Log fillerLog = constructFillerLog(logSpecPool, timeGenerator);
                fillerLogsForPlaceholder.add(fillerLog);
            }
            fillerLogs.add(fillerLogsForPlaceholder);
        }
        return fillerLogs;
    }

    private Log constructFillerLog(Pool<LogSpec> logSpecPool, TimeGenerator timeGenerator) {
        LocalTime time = timeGenerator.generate();
        LogSpec logSpec = logSpecPool.get();
        return new Log(time, logSpec);
    }
}
