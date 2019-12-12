package loggenerator;

import loggenerator.normal.InstrumentConductor;
import loggenerator.normal.instruments.time.SimpleTimeGenerator;
import loggenerator.model.Log;
import loggenerator.suspicious.TroublePipeline;

import java.util.List;
import java.util.stream.Collectors;

public class LogGenerator {
    private InstrumentConductor conductor;
    private TroublePipeline pipeline;

    public LogGenerator(Scenario scenario) {
        int logCount = scenario.getLogCount();
        conductor = new InstrumentConductor(logCount, scenario.getNormalActivities(), scenario.getSubjects());
        pipeline = new TroublePipeline(scenario.getSuspiciousActivities(), scenario.getTroubles(), scenario.getSubjects());
    }

    public List<Log> generate() {
        List<Log.Builder> randomLogs = conductor.orchestrate();
        List<Log.Builder> troubledRandomLogs = pipeline.run(randomLogs);
        return troubledRandomLogs.stream().map(l -> l.withTime(new SimpleTimeGenerator().generate()).build()).collect(Collectors.toList());
    }
}
