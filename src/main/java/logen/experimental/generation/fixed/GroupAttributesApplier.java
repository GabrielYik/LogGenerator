package logen.experimental.generation.fixed;

import logen.experimental.generation.fixed.appliers.GroupFrequencyApplier;
import logen.experimental.generation.fixed.appliers.GroupOrderingApplier;
import logen.experimental.generation.fixed.appliers.GroupSpacingApplier;
import logen.experimental.generation.fixed.appliers.GroupTimePeriodApplier;
import logen.experimental.scenario.group.Group;

import java.util.stream.Stream;

/**
 * An applier that applies the scenario group attributes that define fixed logs on
 * the logs of a group.
 */
public class GroupAttributesApplier {
    private final Group group;

    /**
     * Constructs a group attribute applier from {@code Group}.
     *
     * @param group The group to process
     */
    public GroupAttributesApplier(Group group) {
        this.group = group;
    }

    /**
     * Applies the following scenario group attributes:
     * <ul>
     *     <li>ordering
     *     <li>spacing
     *     <li>time period
     *     <li>frequency
     * </ul>
     * to the logs in the group.
     *
     * @return a group fixture that bundles both the fixed logs and
     * incomplete placeholders generated from the group
     */
    public GroupFixture apply() {
        GroupFixture.Builder groupFixture = new GroupFixture.Builder()
                .setLogSpecs(group.getLogSpecs());
        return Stream.of(groupFixture)
                .map(this::applyFrequency)
                .map(this::applyOrdering)
                .map(this::applyTimePeriod)
                .map(this::applySpacing)
                .findFirst()
                .get()
                .build();
    }

    private GroupFixture.Builder applyOrdering(GroupFixture.Builder groupFixture) {
        groupFixture = new GroupOrderingApplier(
                groupFixture,
                group.getOrdering()
        ).apply();
        return groupFixture;
    }

    private GroupFixture.Builder applySpacing(GroupFixture.Builder groupFixture) {
        groupFixture = new GroupSpacingApplier(
                groupFixture,
                group.getSpacing()
        ).apply();
        return groupFixture;
    }


    private GroupFixture.Builder applyTimePeriod(GroupFixture.Builder groupFixture) {
        groupFixture = new GroupTimePeriodApplier(
                groupFixture,
                group.getTimePeriod()
        ).apply();
        return groupFixture;
    }

    private GroupFixture.Builder applyFrequency(GroupFixture.Builder groupFixture) {
        groupFixture = new GroupFrequencyApplier(
                groupFixture,
                group.getFrequency()
        ).apply();
        return groupFixture;
    }
}
