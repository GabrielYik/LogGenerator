package util;

import logen.scenario.Scenario;
import logen.scenario.common.Frequency;
import logen.scenario.common.LogSpec;
import logen.scenario.group.Group;
import logen.scenario.time.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public class ScenarioBuilder {
    private final Scenario scenario;
    private final List<Group> groups;
    private final List<LogSpec> logSpecs;

    public ScenarioBuilder() {
        this.scenario = new Scenario();
        groups = new ArrayList<>();
        logSpecs = new ArrayList<>();
    }

    public ScenarioBuilder withLogCount(int logCount) {
        scenario.setLogCount(logCount);
        return this;
    }

    public ScenarioBuilder withHeaders(List<String> headers) {
        scenario.setHeaders(headers);
        return this;
    }

    public ScenarioBuilder withTimePeriod(TimePeriod timePeriod) {
        scenario.setTimePeriod(timePeriod);
        return this;
    }

    public ScenarioBuilder withDescriptions(List<String> descriptions) {
        scenario.setDescriptions(descriptions);
        return this;
    }

    public ScenarioBuilder withTypes(List<String> types) {
        scenario.setTypes(types);
        return this;
    }

    public ScenarioBuilder withSubjects(List<String> subjects) {
        scenario.setSubjects(subjects);
        return this;
    }

    public ScenarioBuilder withRemarks(List<String> remarks) {
        scenario.setRemarks(remarks);
        return this;
    }

    public ScenarioBuilder withFrequency(Frequency frequency) {
        scenario.setFrequency(frequency);
        return this;
    }

    public GroupBuilder withGroup() {
        return new GroupBuilder(this);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public LogSpecBuilder withLogSpec() {
        return new LogSpecBuilder(this);
    }

    public void addLogSpec(LogSpec logSpec) {
        logSpecs.add(logSpec);
    }

    public Scenario build() {
        scenario.setGroups(groups);
        scenario.setLogSpecs(logSpecs);
        return scenario;
    }
}
