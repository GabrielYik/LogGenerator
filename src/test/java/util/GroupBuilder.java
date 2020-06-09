package util;

import logen.scenario.common.Frequency;
import logen.scenario.common.LogSpec;
import logen.scenario.group.Group;
import logen.scenario.group.GroupOrdering;
import logen.scenario.group.GroupSpacing;
import logen.scenario.group.GroupTimePeriod;

import java.util.ArrayList;
import java.util.List;

public class GroupBuilder {
    private final Group group;

    private final List<LogSpec> logSpecs;

    private final ScenarioBuilder scenarioBuilder;

    public GroupBuilder() {
        group = new Group();
        logSpecs = new ArrayList<>();
        scenarioBuilder = null;
    }

    public GroupBuilder(ScenarioBuilder scenarioBuilder) {
        group = new Group();
        logSpecs = new ArrayList<>();
        this.scenarioBuilder = scenarioBuilder;
    }

    public GroupBuilder withOrdering(GroupOrdering ordering) {
        group.setOrdering(ordering);
        return this;
    }

    public GroupBuilder withSpacing(GroupSpacing spacing) {
        group.setSpacing(spacing);
        return this;
    }

    public GroupBuilder withTimePeriod(GroupTimePeriod timePeriod) {
        group.setTimePeriod(timePeriod);
        return this;
    }

    public GroupBuilder withDescriptions(List<String> descriptions) {
        group.setDescriptions(descriptions);
        return this;
    }

    public GroupBuilder withTypes(List<String> types) {
        group.setTypes(types);
        return this;
    }

    public GroupBuilder withSubjects(List<String> subjects) {
        group.setSubjects(subjects);
        return this;
    }

    public GroupBuilder withRemarks(List<String> remarks) {
        group.setRemarks(remarks);
        return this;
    }

    public GroupBuilder withFrequency(Frequency frequency) {
        group.setFrequency(frequency);
        return this;
    }

    public LogSpecBuilder scopingLogSpecs() {
        return new LogSpecBuilder(this);
    }

    public void addLogSpec(LogSpec logSpec) {
        logSpecs.add(logSpec);
    }

    public Group build() {
        group.setLogSpecs(logSpecs);
        return group;
    }
}
