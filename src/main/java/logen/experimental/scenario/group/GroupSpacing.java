package logen.experimental.scenario.group;

import java.util.List;

public class GroupSpacing {
    private GroupSpacingType type;
    private List<Integer> amounts;

    public GroupSpacing() {

    }

    public GroupSpacing(GroupSpacingType type, List<Integer> amounts) {
        this.type = type;
        this.amounts = amounts;
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
