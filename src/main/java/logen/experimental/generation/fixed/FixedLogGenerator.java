package logen.experimental.generation.fixed;

import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.group.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A generator of fixed logs.
 * The generator has to be executed before the generation of filler logs.
 */
public class FixedLogGenerator {
    private final Scenario scenario;

    /**
     * Constructs a generator of fixed logs for {@code scenario}.
     * @param scenario The scenario from which the fixed logs are to be generated
     */
    public FixedLogGenerator(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Generates fixed logs and the necessary placeholders.
     * The fixed logs and placeholders are aggregated into a fixture.
     * @return An fixture containing the fixed logs and placeholders.
     */
    public Fixture generate() {
        List<GroupFixture> groupFixtures = new ArrayList<>();
        for (Group group : scenario.getGroups()) {
            GroupFixture groupFixture = new GroupProcessor(group).process();
            groupFixtures.add(groupFixture);
        }
        return null;
    }
}
