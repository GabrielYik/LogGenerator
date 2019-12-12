package loggenerator.suspicious;

import loggenerator.model.Activity;
import loggenerator.model.Log;
import loggenerator.normal.instruments.RandomChooser;

import java.util.ArrayList;
import java.util.List;

public class TroublePipeline {
    private List<TroubleMaker> troubleMakers;

    public TroublePipeline(List<Activity> suspiciousActivities, List<String> troubles, List<String> subjects) {
        troubleMakers = new ArrayList<>();
        for (int i = 0; i < suspiciousActivities.size(); i++) {
            Log.Builder suspiciousLog = Log.builder().withActivity(suspiciousActivities.get(i)).withSubject(subjects.get(i));
            TroubleMakingContext context = new TroubleMakingContext(troubles.get(i), suspiciousLog, RandomChooser.chooseFrom((subjects)));
            TroubleMaker troubleMaker = TroubleMakerFactory.getTroubleMaker(context);
            troubleMakers.add(troubleMaker);
        }
    }

    public List<Log.Builder> run(List<Log.Builder> logs) {
        List<Log.Builder> troubledLogs = logs;
        for (TroubleMaker troubleMaker : troubleMakers) {
            troubledLogs = troubleMaker.makeTrouble(troubledLogs);
        }
        return troubledLogs;
    }
}
