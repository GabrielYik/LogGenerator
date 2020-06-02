package logen.scenario.common;

public class Frequency {
    private FrequencyType type;
    private int count;

    public boolean verifyIfRequiredAttributesSet() {
        return type != null && count != 0;
    }

    public FrequencyType getType() {
        return type;
    }

    public void setType(FrequencyType type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void decrement() {
        count--;
    }

    public boolean isExhausted() {
        return count == 0;
    }
}
