package logen.generation;

import logen.generation.normal.NormalConductor;
import logen.generation.suspicious.SuspiciousConductor;
import logen.log.Log;
import logen.storage.Scenario;
import logen.util.BreakpointChooser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class LogGenerator {
    private int logCount;
    private NormalConductor normalConductor;
    private SuspiciousConductor suspiciousConductor;

    public LogGenerator(Scenario scenario) {
        logCount = scenario.getLogCount();
        normalConductor = new NormalConductor(
            scenario.getNormalActivities(),
            scenario.getSubjects());
        suspiciousConductor = new SuspiciousConductor(
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
                context = suspiciousConductor.orchestrate(context);
            } else {
                context = normalConductor.orchestrate(context, currentLogCount, logCount);
                currentLogCount++;
            }
        }
        return context.getPartialLogs()
                .stream()
                .map(Log.Builder::build)
                .collect(Collectors.toList());
    }

    private ListIterator<Integer> generateBreakpoints() {
        BreakpointChooser chooser = new BreakpointChooser(logCount, suspiciousConductor.getTroubleMakerCount());
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
