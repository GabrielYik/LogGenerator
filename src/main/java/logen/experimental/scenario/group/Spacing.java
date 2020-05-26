package logen.experimental.scenario.group;

import java.util.List;

public class Spacing {
    private SpacingType type;
    private List<Integer> amount;

    public Spacing() {

    }

    public Spacing(SpacingType type, List<Integer> amount) {
        this.type = type;
        this.amount = amount;
    }

    public SpacingType getType() {
        return type;
    }

    public void setType(SpacingType type) {
        this.type = type;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }
}
