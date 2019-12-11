package loggenerator;

import loggenerator.model.Activity;

import java.util.List;

public class Scenario {
    private int logCount;
    private List<String> headers;

    private List<Activity> activities;
    private List<String> subjects;

    private String timeGeneratorType;
    private String activityGeneratorType;
    private String subjectGeneratorType;

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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getTimeGeneratorType() {
        return timeGeneratorType;
    }

    public void setTimeGeneratorType(String timeGeneratorType) {
        this.timeGeneratorType = timeGeneratorType;
    }

    public String getActivityGeneratorType() {
        return activityGeneratorType;
    }

    public void setActivityGeneratorType(String activityGeneratorType) {
        this.activityGeneratorType = activityGeneratorType;
    }

    public String getSubjectGeneratorType() {
        return subjectGeneratorType;
    }

    public void setSubjectGeneratorType(String subjectGeneratorType) {
        this.subjectGeneratorType = subjectGeneratorType;
    }
}
