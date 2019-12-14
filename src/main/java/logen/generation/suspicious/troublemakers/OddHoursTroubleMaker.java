package logen.generation.suspicious.troublemakers;

import java.util.ArrayList;
import java.util.List;
import logen.generation.TransitionContext;
import logen.log.Activity;
import logen.log.Log;
import logen.generation.InstrumentConductor;
import logen.generation.suspicious.Trouble;

public class OddHoursTroubleMaker implements TroubleMaker {
    private Trouble trouble;
    private InstrumentConductor conductor;

    public OddHoursTroubleMaker(Trouble trouble, List<Activity> suspiciousActivities, List<String> subjects) {
        this.trouble = trouble;
        conductor = new InstrumentConductor(suspiciousActivities, subjects);
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        List<Log.Builder> generatedLogs = generateLogs();
        List<Log.Builder> partialLogs = context.getPartialLogs();
        partialLogs.addAll(generatedLogs);
        return new TransitionContext(partialLogs, trouble.getNextTime());
    }

    private List<Log.Builder> generateLogs() {
        int currentCount = 0;
        TransitionContext context = new TransitionContext(new ArrayList<>(), trouble.getTime());
        for (int i = 0; i < trouble.getCount(); i++) {
            context = conductor.orchestrate(context, currentCount, trouble.getCount());
            currentCount++;
        }
        return context.getPartialLogs();
    }
}
