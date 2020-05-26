package logen.experimental.scenario.group;

import java.util.List;

public class Ordering {
    private OrderingType type;
    private List<Integer> sequence;

    public OrderingType getType() {
        return type;
    }

    public void setType(OrderingType type) {
        this.type = type;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }
}
