package logen.experimental.generation.fixed.appliers;

import logen.experimental.generation.fixed.GroupFixture;
import logen.experimental.scenario.common.Frequency;

import java.util.ArrayList;
import java.util.List;

/**
 * An applier that acts on a group by applying the frequency attribute
 * defined on the group on logs in the group.
 */
public class GroupFrequencyApplier extends GroupAttributeApplier {
    private final Frequency frequency;

    /**
     * Constructs a group frequency applier.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     * @param frequency The frequency defined on the logs in the group
     */
    public GroupFrequencyApplier(
            GroupFixture.Builder groupFixture,
            Frequency frequency
    ) {
        super(groupFixture);
        this.frequency = frequency;
    }

    @Override
    public GroupFixture.Builder apply() {
        if (doesNotRequireMultiplication()) {
            return groupFixture;
        }

        List<GroupFixture.Builder> groupFixtures = multipleGroupFixtures();
        return groupFixtures.stream()
                .reduce(GroupFixture.Builder.empty(), GroupFixture.Builder::merge);
    }

    /**
     * Checks if the fixed logs and placeholders of the group
     * need to be multiplied with a factor corresponding to
     * the frequency count defined on the group.
     *
     * @return True if multiplication is required, false otherwise
     */
    private boolean doesNotRequireMultiplication() {
        return frequency.getCount() == 1;
    }

    /**
     * Generates copies of the group fixture which quantity is the
     * frequency count defined on the group.
     *
     * @return Multiple copies of the group fixture
     */
    private List<GroupFixture.Builder> multipleGroupFixtures() {
        List<GroupFixture.Builder> groupFixtures = new ArrayList<>();
        for (int i = 0; i < frequency.getCount(); i++) {
            groupFixtures.add(groupFixture.copy());
        }
        return groupFixtures;
    }
}
