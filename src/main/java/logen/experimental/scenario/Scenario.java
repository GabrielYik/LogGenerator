package logen.experimental.scenario;

import java.util.List;

public class Scenario {
    private int logCount;
    private List<String> headers;
    private TimePeriod timePeriod;
    private List<String> subjects;

    private List<Group> groups;

    private List<LogSpec> logSpecs;

    public void validate() {

    }

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

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<LogSpec> getLogSpecs() {
        return logSpecs;
    }

    public void setLogSpecs(List<LogSpec> logSpecs) {
        this.logSpecs = logSpecs;
    }
}
