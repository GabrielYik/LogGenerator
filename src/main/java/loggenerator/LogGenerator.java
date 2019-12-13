package loggenerator;

import loggenerator.normal.InstrumentConductor;
import loggenerator.model.Log;
import loggenerator.suspicious.TroublePipeline;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class LogGenerator {
    private int logCount;
    private InstrumentConductor conductor;
    private TroublePipeline pipeline;

    public LogGenerator(Scenario scenario) {
        logCount = scenario.getLogCount();
        conductor = new InstrumentConductor(scenario.getNormalActivities(), scenario.getSubjects());
        pipeline = new TroublePipeline(scenario.getSuspiciousActivities(), scenario.getTroubles(), scenario.getSubjects());
    }

    public List<Log> generate() {
        int currentLogCount = 0;
        List<Integer> breakpoints = generateBreakpoints();
        ListIterator<Integer> breakpointsIterator = breakpoints.listIterator();
        while (currentLogCount != logCount) {
            if (hasReachedBreakpoint(currentLogCount, breakpointsIterator)) {
                List<Log.Builder> partialLogs = conductor.getPartialLogsSoFar();
                partialLogs = pipeline.runNext(partialLogs);
                conductor.resume(partialLogs);
            } else {
                conductor.orchestrate(currentLogCount, logCount);
                currentLogCount++;
            }
        }
        return conductor.getPartialLogsSoFar()
                .stream()
                .map(Log.Builder::build)
                .collect(Collectors.toList());
    }

    private List<Integer> generateBreakpoints() {
        BreakpointChooser chooser = new BreakpointChooser(logCount, pipeline.getTroubleMakerCount());
        return chooser.generateBreakPoints();
    }

    private boolean hasReachedBreakpoint(int current, ListIterator<Integer> breakpoints) {
        if (breakpoints.hasNext()) {
            int breakpoint = breakpoints.next();
            if (current == breakpoint) {
                return true;
            } else {
                breakpoints.previous();
            }
        }
        return false;
    }
}
