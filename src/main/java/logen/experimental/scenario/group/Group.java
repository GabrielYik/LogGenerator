package logen.experimental.scenario.group;

import logen.experimental.scenario.common.Frequency;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.time.TimePeriod;

import java.util.List;

public class Group {
    private String identifier;
    private List<Integer> order;
    private Space space;
    private TimePeriod timePeriod;
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Frequency frequency;

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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public List<LogSpec> getLogSpecs() {
        return logSpecs;
    }

    public void setLogSpecs(List<LogSpec> logSpecs) {
        this.logSpecs = logSpecs;
    }
}
