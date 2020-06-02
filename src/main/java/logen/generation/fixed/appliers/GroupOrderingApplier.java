package logen.generation.fixed.appliers;

import logen.generation.fixed.GroupFixture;
import logen.scenario.common.LogSpec;
import logen.scenario.group.GroupOrdering;

import java.util.ArrayList;
import java.util.List;

/**
 * An applier that acts on a group by applying the ordering attribute
 * defined on the group onto the log specifications in the group.
 */
public class GroupOrderingApplier extends GroupAttributeApplier {
    /**
     * The offset needed to convert a one-based index to a zero-based index.
     */
    private static final int ONE_TO_ZERO_BASED_INDEX_OFFSET = 1;

    private final GroupOrdering ordering;
    private final List<LogSpec> logSpecs;

    /**
     * Constructs a group ordering applier.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     * @param ordering The ordering defined on the logs in the group
     */
    public GroupOrderingApplier(
            GroupFixture.Builder groupFixture,
            GroupOrdering ordering
    ) {
        super(groupFixture);
        this.ordering = ordering;
        this.logSpecs = groupFixture.getLogSpecs();
    }

    @Override
    public GroupFixture.Builder apply() {
        List<LogSpec> orderedLogSpecs = orderLogSpecs();
        return groupFixture.setLogSpecs(orderedLogSpecs);
    }

    /**
     * Orders the log specifications in the group according to their
     * respective positions.
     * @return The log specifications ordered
     */
    private List<LogSpec> orderLogSpecs() {
        List<Integer> positions = ordering.getPositions();
        List<LogSpec> orderedLogSpecs = new ArrayList<>(logSpecs.size());
        for (int i = 0; i < positions.size(); i++) {
            int index = mapPositionToIndex(positions.get(i));
            LogSpec logSpec = logSpecs.get(i);
            orderedLogSpecs.add(index, logSpec);
        }
        return orderedLogSpecs;
    }

    private int mapPositionToIndex(int position) {
        return position - ONE_TO_ZERO_BASED_INDEX_OFFSET;
    }
}
