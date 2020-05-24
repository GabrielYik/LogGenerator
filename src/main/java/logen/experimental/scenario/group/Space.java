package logen.experimental.scenario.group;

import java.util.ArrayList;
import java.util.List;

public class Space {
    public static final int AMOUNT_ANY = -1;

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
