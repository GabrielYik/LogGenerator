package logen.experimental.scenario;

import java.util.List;

public class Group {
    private String identifier;
    private List<Integer> order;
    private List<String> spacings;
    private TimePeriod timePeriod;
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private String frequency;

    private List<LogSpec> logSpecs;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    public List<String> getSpacings() {
        return spacings;
    }

    public void setSpacings(List<String> spacings) {
        this.spacings = spacings;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<LogSpec> getLogSpecs() {
        return logSpecs;
    }

    public void setLogSpecs(List<LogSpec> logSpecs) {
        this.logSpecs = logSpecs;
    }
}
