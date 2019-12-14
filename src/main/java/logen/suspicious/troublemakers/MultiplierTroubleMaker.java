package logen.suspicious.troublemakers;

import logen.TransitionContext;
import logen.model.Activity;
import logen.model.Log;
import logen.normal.instruments.ArbitraryTimeGenerator;
import logen.suspicious.Trouble;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MultiplierTroubleMaker implements TroubleMaker {
    private Trouble trouble;

    private Log.Builder suspiciousLog;

    public MultiplierTroubleMaker(Trouble trouble, Activity suspiciousActivity, String subject) {
        this.trouble = trouble;
        suspiciousLog = Log.builder()
            .withActivity(suspiciousActivity)
            .withSubject(subject);
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        LocalTime previousTime = context.getPreviousTime();
        List<Log.Builder> logCopies = generateCopies(suspiciousLog, previousTime);
        int last = logCopies.size() - 1;
        LocalTime newPreviousTime = logCopies.get(last).getTime();

        List<Log.Builder> partialLogs = context.getPartialLogs();
        partialLogs.addAll(logCopies);
        return new TransitionContext(partialLogs, newPreviousTime);
    }

    private List<Log.Builder> generateCopies(Log.Builder suspiciousLog, LocalTime previousTime) {
        List<Log.Builder> copies = new ArrayList<>();
        LocalTime base = previousTime;
        for (int i = 0; i < trouble.getCount(); i++) {
            Log.Builder copy = (Log.Builder) suspiciousLog.clone();
            base = ArbitraryTimeGenerator.generateFrom(base);
            copy.withTime(base);
            copies.add(copy);
        }
        return copies;
    }
}
