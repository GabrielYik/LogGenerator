package logen.generation.suspicious.troublemakers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import logen.generation.TransitionContext;
import logen.log.Activity;
import logen.log.Log;
import logen.generation.normal.NormalConductor;
import logen.generation.suspicious.Trouble;
import logen.util.TemporalGenerator;

public class OddHoursTroubleMaker implements TroubleMaker {
    private Trouble trouble;
    private NormalConductor normalConductor;

    public OddHoursTroubleMaker(
        Trouble trouble,
        List<Activity> suspiciousActivities,
        List<String> subjects) {
        this.trouble = trouble;
        TemporalGenerator temporalGenerator = new TemporalGenerator(
            LocalDateTime.of(LocalDate.now(), trouble.getStartTime()),
            LocalDateTime.of(LocalDate.now(), trouble.getEndTime())
        );
        normalConductor = new NormalConductor(suspiciousActivities, subjects, temporalGenerator);
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        List<Log.Builder> generatedLogs = generateLogs();
        List<Log.Builder> partialLogs = context.getPartialLogs();
        partialLogs.addAll(generatedLogs);
        return new TransitionContext(partialLogs);
    }

    private List<Log.Builder> generateLogs() {
        int currentCount = 0;
        TransitionContext context = new TransitionContext(new ArrayList<>());
        for (int i = 0; i < trouble.getCount(); i++) {
            context = normalConductor.orchestrate(context, currentCount, trouble.getCount());
            currentCount++;
        }
        return context.getPartialLogs();
    }
}
