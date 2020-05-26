package logen.experimental.scenario.common;

/**
 * A specification for a log.
 */
public class LogSpec {
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Frequency frequency;

    public void preprocess() {
        FrequencyType frequencyType = frequency.getType();
        switch(frequencyType) {
            case ANY:
                frequency.setCount(Integer.MAX_VALUE);
                break;
            case CUSTOM:
                // do nothing
                break;
            default:
                throw new AssertionError();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionIfAbsent(String description) {
        if (this.description == null) {
            this.description = description;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeIfAbsent(String type) {
        if (this.type == null) {
            this.type = type;
        }
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

    public void setRemarksIfAbsent(String remarks) {
        if (this.remarks == null) {
            this.remarks = remarks;
        }
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void setFrequencyIfAbsent(Frequency frequency) {
        if (this.frequency == null) {
            this.frequency = frequency;
        }
    }

    public void decrementFrequency() {
        frequency.decrement();
    }

    public boolean isExhausted() {
        return frequency.isExhausted();
    }
}
