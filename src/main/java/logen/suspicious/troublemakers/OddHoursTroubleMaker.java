package logen.suspicious.troublemakers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import logen.TransitionContext;
import logen.model.Activity;
import logen.model.Log;
import logen.normal.InstrumentConductor;

public class OddHoursTroubleMaker implements TroubleMaker {
    private static int COPY_COUNT = 4;

    private InstrumentConductor conductor;

    public OddHoursTroubleMaker(List<Activity> suspiciousActivities, List<String> subjects) {
        conductor = new InstrumentConductor(suspiciousActivities, subjects);
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        List<Log.Builder> generatedLogs = generateLogs();
        List<Log.Builder> partialLogs = context.getPartialLogs();
        partialLogs.addAll(generatedLogs);
        int last = generatedLogs.size() - 1;
        return new TransitionContext(partialLogs, LocalTime.MIDNIGHT.plusHours(7));
    }

    private List<Log.Builder> generateLogs() {
        int currentCount = 0;
        TransitionContext context = new TransitionContext(new ArrayList<>(), LocalTime.MIDNIGHT.plusHours(2));
        for (int i = 0; i < COPY_COUNT; i++) {
            context = conductor.orchestrate(context, currentCount, COPY_COUNT);
            currentCount++;
        }
        return context.getPartialLogs();
    }
}
