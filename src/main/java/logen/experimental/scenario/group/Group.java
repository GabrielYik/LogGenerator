package logen.experimental.scenario.group;

import logen.experimental.scenario.common.Frequency;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.common.PropagationContainer;
import logen.experimental.scenario.time.TimePeriod;

import java.util.Collections;
import java.util.List;

public class Group {
    /**
     * Not required.
     */
    private GroupOrdering ordering;
    /**
     * Not required.
     */
    private GroupSpacing spacing;
    /**
     * Not required.
     */
    private GroupTimePeriod timePeriod;
    /**
     * Not required.
     */
    private List<String> descriptions;
    /**
     * Not required.
     */
    private List<String> types;
    /**
     * Not required.
     */
    private List<String> subjects;
    /**
     * Not required.
     */
    private List<String> remarks;
    /**
     * Not required.
     */
    private Frequency frequency;
    /**
     * Required.
     */
    private List<LogSpec> logSpecs;

    public void setAttributesIfAbsent(TimePeriod globalTimePeriod) {
        GroupOrdering.setAttributesIfAbsent(ordering, logSpecs.size());
        GroupSpacing.setAttributesIfAbsent(spacing);
        GroupTimePeriod.setAttributesIfAbsent(timePeriod, globalTimePeriod);

        descriptions = setAttributeIfAbsent(descriptions);
        types = setAttributeIfAbsent(types);
        subjects = setAttributeIfAbsent(subjects);
        remarks = setAttributeIfAbsent(remarks);
    }

    private List<String> setAttributeIfAbsent(List<String> property) {
        if (property == null) {
            property = Collections.emptyList();
        }
        return property;
    }

    public void propagateValuesIfAbsent(PropagationContainer scenarioContainer) {
        descriptions = setAttributeIfAbsent(descriptions, scenarioContainer.getDescriptions());
        types = setAttributeIfAbsent(types, scenarioContainer.getTypes());
        subjects = setAttributeIfAbsent(subjects, scenarioContainer.getSubjects());
        remarks = setAttributeIfAbsent(remarks, scenarioContainer.getRemarks());
        PropagationContainer groupContainer = toPropagationContainer();
        logSpecs.forEach(logSpec -> logSpec.propagateValuesIfAbsent(groupContainer));
    }

    private <E> List<E> setAttributeIfAbsent(List<E> attribute, List<E> newAttribute) {
        if (attribute.isEmpty()) {
            attribute = newAttribute;
        }
        return attribute;
    }

    private PropagationContainer toPropagationContainer() {
        return new PropagationContainer(
                descriptions,
                types,
                subjects,
                remarks,
                frequency
        );
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

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
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
