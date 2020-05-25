package logen.experimental.generation.fixed;

import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.group.Group;

import java.util.ArrayList;
import java.util.List;

public class FixedLogGenerator {
    private final Scenario scenario;

    public FixedLogGenerator(Scenario scenario) {
        this.scenario = scenario;
    }

    public Fixture generate() {
        List<GroupFixture> groupFixtures = new ArrayList<>();
        for (Group group : scenario.getGroups()) {
            GroupFixture groupFixture = new GroupProcessor(group).process();
            groupFixtures.add(groupFixture);
        }
        return null;
    }
}
