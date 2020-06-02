package logen.experimental.scenario.group;

import java.util.List;

public class GroupSpacing {
    private static final GroupSpacingType DEFAULT_TYPE = GroupSpacingType.ANY;

    private GroupSpacingType type;
    private List<Integer> amounts;

    public static void setAttributesIfAbsent(GroupSpacing spacing) {
        if (spacing == null) {
            spacing = new GroupSpacing();
            spacing.type = DEFAULT_TYPE;
        }

        if (spacing.type == null && spacing.amounts == null) {
            spacing.type = DEFAULT_TYPE;
            return;
        }

        if (spacing.type == null && spacing.amounts != null) {
            spacing.type = DEFAULT_TYPE;
        }
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
