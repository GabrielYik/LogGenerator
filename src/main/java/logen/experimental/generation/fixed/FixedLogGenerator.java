package logen.experimental.generation.fixed;

import logen.experimental.log.Log;
import logen.experimental.scenario.Scenario;
import logen.experimental.scenario.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            GroupFixture groupFixture = new GroupAttributesApplier(group).apply();
            groupFixtures.add(groupFixture);
        }
        return constructFixture(groupFixtures);

    }

    public Fixture constructFixture(List<GroupFixture> groupFixtures) {
        GroupFixture consolidatedGroupFixture = groupFixtures.stream()
                .reduce(GroupFixture.empty(), GroupFixture::merge);
        List<Log> logs = consolidatedGroupFixture.getLogs();
        List<Placeholder> placeholders = consolidatedGroupFixture.getPlaceholders().stream()
                .map(Placeholder.Builder::build)
                .collect(Collectors.toList());
        return new Fixture(logs, placeholders);
    }
}
