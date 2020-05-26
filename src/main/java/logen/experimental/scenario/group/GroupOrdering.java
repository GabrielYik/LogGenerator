package logen.experimental.scenario.group;

import java.util.List;

public class GroupOrdering {
    private GroupOrderingType type;
    private List<Integer> sequence;

    public GroupOrderingType getType() {
        return type;
    }

    public void setType(GroupOrderingType type) {
        this.type = type;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }
}
