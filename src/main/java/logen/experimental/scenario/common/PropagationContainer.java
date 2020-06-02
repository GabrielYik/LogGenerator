package logen.experimental.scenario.common;

import java.util.List;

public class PropagationContainer {
    private final List<String> descriptions;
    private final List<String> types;
    private final List<String> subjects;
    private final List<String> remarks;
    private final Frequency frequency;

    public PropagationContainer(
            List<String> descriptions,
            List<String> types,
            List<String> subjects,
            List<String> remarks,
            Frequency frequency
    ) {
        this.descriptions = descriptions;
        this.types = types;
        this.subjects = subjects;
        this.remarks = remarks;
        this.frequency = frequency;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public Frequency getFrequency() {
        return frequency;
    }
}
