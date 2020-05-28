package logen.experimental.scenario.group;

import java.util.List;

public class GroupOrdering {
    private GroupOrderingType type;
    private List<Integer> positions;

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
