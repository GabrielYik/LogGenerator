package logen.suspicious;

import java.util.List;
import logen.model.Activity;

public class TroubleMakingContext {
    private Trouble trouble;
    private List<Activity> suspiciousActivities;
    private List<String> subjects;

    public TroubleMakingContext(Trouble trouble, List<Activity> suspiciousActivities, List<String> subjects) {
        this.trouble = trouble;
        this.suspiciousActivities = suspiciousActivities;
        this.subjects = subjects;
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
}
