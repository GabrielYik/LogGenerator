package logen.experimental.scenario.group;

import java.util.List;

public class Order {
    private OrderType type;
    private List<Integer> sequence;

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }
}
