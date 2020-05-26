package logen.experimental.scenario.group;

import java.util.List;

public class GroupSpacing {
    private GroupSpacingType type;
    private List<Integer> amount;

    public GroupSpacing() {

    }

    public GroupSpacing(GroupSpacingType type, List<Integer> amount) {
        this.type = type;
        this.amount = amount;
    }

    public GroupSpacingType getType() {
        return type;
    }

    public void setType(GroupSpacingType type) {
        this.type = type;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }
}
