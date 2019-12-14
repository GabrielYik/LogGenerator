package logen.suspicious;

import java.util.List;
import logen.model.Activity;
import logen.model.Log;

public class TroubleMakingContext {
    private String type;
    private List<Activity> suspiciousActivities;
    private List<String> subjects;

    public TroubleMakingContext(String type, List<Activity> suspiciousActivities, List<String> subjects) {
        this.type = type;
        this.suspiciousActivities = suspiciousActivities;
        this.subjects = subjects;
    }

    public String getType() {
        return type;
    }

    public List<Activity> getSuspiciousActivities() {
        return suspiciousActivities;
    }

    public List<String> getSubjects() {
        return subjects;
    }
}
