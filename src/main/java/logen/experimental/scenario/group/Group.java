package logen.experimental.scenario.group;

import javafx.util.Pair;
import logen.experimental.scenario.common.Frequency;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.time.TimePeriod;
import logen.experimental.util.RandomChooser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    private String identifier;
    private GroupOrdering ordering;
    private GroupSpacing spacing;
    private GroupTimePeriod timePeriod;
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Frequency frequency;

    private List<LogSpec> logSpecs;

    public void preprocess(TimePeriod globalTimePeriod) {
        preprocessOrder();
        preprocessTimePeriod(globalTimePeriod);
        preprocessLogSpecs();
    }

    private void preprocessOrder() {
        switch(ordering.getType()) {
            case ANY:
                List<Integer> sequence = new ArrayList<>(logSpecs.size());
                int counter = 1;
                for (int i = 0; i < logSpecs.size(); i++) {
                    sequence.add(counter++);
                }
                Collections.shuffle(sequence);
                ordering.setSequence(sequence);
                break;
            case CUSTOM:
                // do nothing
                break;
            default:
                throw new AssertionError();
        }
    }

    private void preprocessTimePeriod(TimePeriod globalTimePeriod) {
        LocalTime startTime;
        LocalTime endTime;
        switch(timePeriod.getType()) {
            case ANY:
                startTime = RandomChooser.chooseBetween(
                        globalTimePeriod.getStartTime(),
                        globalTimePeriod.getEndTime().minusHours(2)
                );
                endTime = startTime.plusHours(2);
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case CUSTOM:
                // do nothing
                break;
            case ONE_HOUR:
                startTime = RandomChooser.chooseBetween(
                        globalTimePeriod.getStartTime(),
                        globalTimePeriod.getEndTime().minusHours(1)
                );
                endTime = startTime.plusHours(1);
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case ONE_DAY:
                startTime = globalTimePeriod.getStartTime();
                endTime = globalTimePeriod.getEndTime();
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case AFTER_MIDNIGHT:
                Pair<LocalTime, LocalTime> startEndTime = GroupTimePeriodType.map(GroupTimePeriodType.AFTER_MIDNIGHT);
                startTime = startEndTime.getKey();
                endTime = startEndTime.getValue();
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void preprocessLogSpecs() {
        logSpecs.forEach(LogSpec::preprocess);
        overrideLocalAttributes();
    }

    private void overrideLocalAttributes() {
        for (LogSpec logSpec : logSpecs) {
            if (description != null) {
                logSpec.setDescriptionIfAbsent(description);
            }
            if (type != null) {
                logSpec.setTypeIfAbsent(type);
            }
            if (subject != null) {
                logSpec.setSubjectIfAbsent(subject);
            }
            if (remarks != null) {
                logSpec.setRemarksIfAbsent(remarks);
            }
            if (frequency != null) {
                logSpec.setFrequencyIfAbsent(frequency);
            }
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public GroupOrdering getOrdering() {
        return ordering;
    }

    public void setOrdering(GroupOrdering ordering) {
        this.ordering = ordering;
    }

    public GroupSpacing getSpacing() {
        return spacing;
    }

    public void setSpacing(GroupSpacing spacing) {
        this.spacing = spacing;
    }

    public GroupTimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(GroupTimePeriod timePeriod) {
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
