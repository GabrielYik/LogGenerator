package util;

import logen.scenario.common.Frequency;
import logen.scenario.common.LogSpec;

public class LogSpecBuilder {
    private final LogSpec logSpec;

    private final ScenarioBuilder scenarioBuilder;
    private final GroupBuilder groupBuilder;

    public LogSpecBuilder(ScenarioBuilder scenarioBuilder) {
        this.scenarioBuilder = scenarioBuilder;
        this.groupBuilder = null;
        this.logSpec = new LogSpec();
    }

    public LogSpecBuilder(GroupBuilder groupBuilder) {
        this.scenarioBuilder = null;
        this.groupBuilder = groupBuilder;
        this.logSpec = new LogSpec();
    }

    public LogSpecBuilder withDescription(String description) {
        logSpec.setDescription(description);
        return this;
    }

    public LogSpecBuilder withType(String type) {
        logSpec.setType(type);
        return this;
    }

    public LogSpecBuilder withSubject(String subject) {
        logSpec.setSubject(subject);
        return this;
    }

    public LogSpecBuilder withRemark(String remark) {
        logSpec.setRemark(remark);
        return this;
    }

    public LogSpecBuilder withFrequency(Frequency frequency) {
        logSpec.setFrequency(frequency);
        return this;
    }

    public GroupBuilder inGroup() {
        groupBuilder.addLogSpec(logSpec);
        return groupBuilder;
    }
}
