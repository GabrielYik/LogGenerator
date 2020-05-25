package logen.experimental.scenario.group;

import java.util.List;

public class Space {
    private SpaceType type;
    private List<Integer> amount;

    public SpaceType getType() {
        return type;
    }

    public void setType(SpaceType type) {
        this.type = type;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }
}
