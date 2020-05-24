package logen.experimental.scenario.common;

public class LogSpec {
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Frequency frequency;

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

    public void setSubjectIfAbsent(String subject) {
        //TODO check if null or empty
        if (this.subject == null) {
            this.subject = subject;
        }
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
}
