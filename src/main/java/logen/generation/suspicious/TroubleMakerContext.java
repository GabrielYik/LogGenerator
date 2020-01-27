package logen.generation.suspicious;

import java.util.List;
import logen.log.Activity;
import logen.util.TemporalGenerator;

public class TroubleMakerContext {
    private Trouble trouble;
    private List<Activity> suspiciousActivities;
    private List<String> subjects;
    private TemporalGenerator temporalGenerator;

    public TroubleMakerContext(
        Trouble trouble,
        List<Activity> suspiciousActivities,
        List<String> subjects,
        TemporalGenerator temporalGenerator) {
        this.trouble = trouble;
        this.suspiciousActivities = suspiciousActivities;
        this.subjects = subjects;
        this.temporalGenerator = temporalGenerator;
    }

    public Trouble getTrouble() {
        return trouble;
    }

    public List<Activity> getSuspiciousActivities() {
        return suspiciousActivities;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public TemporalGenerator getTemporalGenerator() {
        return temporalGenerator;
    }
}
