package logen.experimental.scenario.common;

import logen.experimental.util.RandomUtil;

import java.util.List;

/**
 * A specification for a log.
 */
public class LogSpec {
    private String description;
    private String type;
    private String subject;
    private String remark;
    private Frequency frequency;

    public void propagateValuesIfAbsent(PropagationContainer container) {
        description = setAttributeIfAbsent(description, container.getDescriptions());
        type = setAttributeIfAbsent(type, container.getTypes());
        subject = setAttributeIfAbsent(subject, container.getSubjects());
        remark = setAttributeIfAbsent(remark, container.getRemarks());
        if (frequency == null) frequency = container.getFrequency();
    }

    private <E> E setAttributeIfAbsent(E attribute, List<E> valueSource) {
        if (attribute == null) {
            attribute = RandomUtil.chooseFrom(valueSource);
        }
        return attribute;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRemarksIfAbsent(String remarks) {
        if (this.remark == null) {
            this.remark = remarks;
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
