package logen.scenario.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupOrdering {
    private static final GroupOrderingType DEFAULT_TYPE = GroupOrderingType.ANY;

    private GroupOrderingType type;
    private List<Integer> positions;

    /**
     * Sets the attribute or sub-attributes of {@code ordering} if any
     * are missing.
     *
     * At this point, there are 3 cases of missing sub-attributes that can
     * be handled:
     * <ol>
     *     <li>Type and positions
     *     <li>Type
     *     <li>Given type of {@code ANY}, positions
     * </ol>
     *
     * @param ordering The attribute
     * @param logSpecCount The number of log specifications for computation
     *                     of default positions
     */
    public static void setAttributesIfAbsent(GroupOrdering ordering, int logSpecCount) {
        if (ordering == null) {
            ordering = new GroupOrdering();
        }

        if (ordering.type == null && ordering.positions == null) {
            ordering.type = DEFAULT_TYPE;
            ordering.positions = computeDefaultPositions(logSpecCount);
            return;
        }

        if (ordering.type == null && ordering.positions != null) {
            ordering.type = GroupOrderingType.CUSTOM;
            return;
        }

        if (ordering.type == DEFAULT_TYPE && ordering.positions == null) {
            ordering.positions = computeDefaultPositions(logSpecCount);
        }
    }

    /**
     * Computes a randomised list of size {@code logSpecCount} of integers
     * 1 to {@code logSpecCount}.
     *
     * @param logSpecCount The size of the resulting list
     * @return A randomised list of size {@code logSpecCount}
     */
    private static List<Integer> computeDefaultPositions(int logSpecCount) {
        List<Integer> positions = new ArrayList<>(logSpecCount);
        int position = 1;
        for (int i = 0; i < logSpecCount; i++) {
            positions.add(position++);
        }
        Collections.shuffle(positions);
        return positions;
    }

    public GroupOrderingType getType() {
        return type;
    }

    public void setType(GroupOrderingType type) {
        this.type = type;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }
}
