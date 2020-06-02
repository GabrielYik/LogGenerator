package logen.experimental.scenario.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupOrdering {
    private static final GroupOrderingType DEFAULT_TYPE = GroupOrderingType.ANY;

    private GroupOrderingType type;
    private List<Integer> positions;

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
            ordering.type = DEFAULT_TYPE;
            return;
        }

        if (ordering.type == DEFAULT_TYPE && ordering.positions == null) {
            ordering.positions = computeDefaultPositions(logSpecCount);
        }
    }

    private static List<Integer> computeDefaultPositions(int logSpecCount) {
        List<Integer> positions = new ArrayList<>(logSpecCount);
        int position = 1;
        for (int i = 0; i < logSpecCount; i++) {
            positions.add(position++);
        }
        Collections.shuffle(positions);
        return positions;
    }

    public boolean isComplete() {
        return type != null && positions != null;
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
