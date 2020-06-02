package logen.scenario.group;

import java.util.ArrayList;
import java.util.List;

public class GroupSpacing {
    private static final GroupSpacingType DEFAULT_TYPE = GroupSpacingType.ANY;
    private static final int DUMMY_AMOUNT = -1;

    private GroupSpacingType type;
    private List<Integer> amounts;

    public static void setAttributesIfAbsent(GroupSpacing spacing, int logSpecCount) {
        if (spacing == null) {
            spacing = new GroupSpacing();
            spacing.type = DEFAULT_TYPE;
        }

        if (spacing.type == null && spacing.amounts == null) {
            spacing.type = DEFAULT_TYPE;
            spacing.amounts = computeDummyAmounts(logSpecCount);
            return;
        }

        if (spacing.type == null && spacing.amounts != null) {
            spacing.type = GroupSpacingType.CUSTOM;
        }

        if (spacing.type == DEFAULT_TYPE && spacing.amounts == null) {
            spacing.amounts = computeDummyAmounts(logSpecCount);
        }
    }

    private static List<Integer> computeDummyAmounts(int logSpecCount) {
        List<Integer> positions = new ArrayList<>(logSpecCount - 1);
        for (int i = 0; i < logSpecCount; i++) {
            positions.add(DUMMY_AMOUNT);
        }
        return positions;
    }

    public GroupSpacingType getType() {
        return type;
    }

    public void setType(GroupSpacingType type) {
        this.type = type;
    }

    public List<Integer> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Integer> amounts) {
        this.amounts = amounts;
    }
}
