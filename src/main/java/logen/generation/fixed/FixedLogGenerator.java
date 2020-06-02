package logen.generation.fixed;

import logen.log.Log;
import logen.scenario.Scenario;
import logen.scenario.group.Group;

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
        List<GroupFixture> groupFixtures = applyGroupAttributes();
        GroupFixture consolidated = consolidateGroupFixtures(groupFixtures);
        GroupFixture completedGroupFixture = completeGroupFixtureConstruction(consolidated);
        return constructFixture(completedGroupFixture);
    }

    /**
     * Generates group fixtures by applying scenario group attributes
     * that define fixed logs on the logs of a group.
     *
     * @return The group fixtures
     */
    private List<GroupFixture> applyGroupAttributes() {
        List<GroupFixture> groupFixtures = new ArrayList<>();
        for (Group group : scenario.getGroups()) {
            GroupFixture groupFixture = new GroupAttributesApplier(group).apply();
            groupFixtures.add(groupFixture);
        }
        return groupFixtures;
    }

    /**
     * Consolidate {@code groupFixtures} into a single group fixture.
     *
     * @param groupFixtures The group fixtures
     * @return A consolidated group fixture from {@code groupFixtures}
     */
    private GroupFixture consolidateGroupFixtures(List<GroupFixture> groupFixtures) {
        return groupFixtures.stream()
                .reduce(GroupFixture.empty(), GroupFixture::merge);
    }

    /**
     * Completes the construction of {@code groupFixture}.
     *
     * @param groupFixture A mostly constructed group fixture
     * @return A completely constructed group fixture
     */
    private GroupFixture completeGroupFixtureConstruction(GroupFixture groupFixture) {
        return new PlaceholderLogCountSetter(scenario, groupFixture).set();
    }

    /**
     * Constructs a fixture from {@code groupFixture}.
     *
     * @param groupFixture The group fixture
     * @return A fixture
     */
    private Fixture constructFixture(GroupFixture groupFixture) {
        List<Log> fixedLogs = groupFixture.getFixedLogs();
        List<Placeholder> placeholders = groupFixture.getPlaceholders().stream()
                .map(Placeholder.Builder::build)
                .collect(Collectors.toList());
        return new Fixture(fixedLogs, placeholders);
    }
}
