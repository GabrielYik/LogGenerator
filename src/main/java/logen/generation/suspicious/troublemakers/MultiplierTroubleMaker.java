package logen.generation.suspicious.troublemakers;

import logen.generation.TransitionContext;
import logen.log.Activity;
import logen.log.Log;
import logen.util.RandomChooser;
import logen.util.TemporalGenerator;
import logen.generation.suspicious.Trouble;

import java.util.ArrayList;
import java.util.List;

public class MultiplierTroubleMaker implements TroubleMaker {
    private Trouble trouble;
    private TemporalGenerator temporalGenerator;

    private Log.Builder suspiciousLog;

    public MultiplierTroubleMaker(
        Trouble trouble,
        Activity suspiciousActivity,
        List<String> subjects,
        TemporalGenerator temporalGenerator) {
        this.trouble = trouble;
        this.temporalGenerator = temporalGenerator;
        if (!suspiciousActivity.hasSubject()) {
            suspiciousActivity.setSubject(RandomChooser.chooseFrom(subjects));
        }
        suspiciousLog = Log.builder()
            .withActivity(suspiciousActivity);
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        List<Log.Builder> logCopies = generateCopies(suspiciousLog);
        int last = logCopies.size() - 1;

        List<Log.Builder> partialLogs = context.getPartialLogs();
        partialLogs.addAll(logCopies);
        return new TransitionContext(partialLogs);
    }

    private List<Log.Builder> generateCopies(Log.Builder suspiciousLog) {
        List<Log.Builder> copies = new ArrayList<>();
        for (int i = 0; i < trouble.getCount(); i++) {
            Log.Builder copy = (Log.Builder) suspiciousLog.clone();
            copy.withTime(temporalGenerator.generateTime());
            copies.add(copy);
        }
        return copies;
    }
}
