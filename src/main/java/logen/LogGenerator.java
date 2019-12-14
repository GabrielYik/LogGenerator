package logen;

import logen.model.Log;
import logen.normal.InstrumentConductor;
import logen.storage.Scenario;
import logen.suspicious.TroublePipeline;
import logen.util.BreakpointChooser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class LogGenerator {
    private int logCount;
    private InstrumentConductor conductor;
    private TroublePipeline pipeline;

    public LogGenerator(Scenario scenario) {
        logCount = scenario.getLogCount();
        conductor = new InstrumentConductor(
            scenario.getNormalActivities(),
            scenario.getSubjects());
        pipeline = new TroublePipeline(
            scenario.getSuspiciousActivities(),
            scenario.getTroubles(),
            scenario.getSubjects());
    }

    public List<Log> generate() {
        ListIterator<Integer> breakpoints = generateBreakpoints();
        int currentLogCount = 0;
        TransitionContext context = new TransitionContext(new ArrayList<>(), LocalTime.now());
        while (currentLogCount != logCount) {
            if (hasReachedBreakpoint(currentLogCount, breakpoints)) {
                context = pipeline.runNext(context);
            } else {
                context = conductor.orchestrate(context, currentLogCount, logCount);
                currentLogCount++;
            }
        }
        return context.getPartialLogs()
                .stream()
                .map(Log.Builder::build)
                .collect(Collectors.toList());
    }

    private ListIterator<Integer> generateBreakpoints() {
        BreakpointChooser chooser = new BreakpointChooser(logCount, pipeline.getTroubleMakerCount());
        return chooser.generateBreakpoints();
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
