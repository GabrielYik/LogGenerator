package logen.storage;

import logen.model.Activity;
import logen.suspicious.Trouble;

import java.util.List;

public class Scenario {
    private int logCount;
    private List<String> headers;

    private List<Activity> normalActivities;
    private List<Activity> suspiciousActivities;
    private List<String> subjects;

    private List<Trouble> troubles;

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Activity> getNormalActivities() {
        return normalActivities;
    }

    public void setNormalActivities(List<Activity> normalActivities) {
        this.normalActivities = normalActivities;
    }

    public List<Activity> getSuspiciousActivities() {
        return suspiciousActivities;
    }

    public void setSuspiciousActivities(List<Activity> suspiciousActivities) {
        this.suspiciousActivities = suspiciousActivities;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Trouble> getTroubles() {
        return troubles;
    }

    public void setTroubles(List<Trouble> troubles) {
        this.troubles = troubles;
    }
}
