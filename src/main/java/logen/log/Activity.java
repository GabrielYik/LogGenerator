package logen.log;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String name;
    private String type;
    private String remarks;
    private Activity complement;

    public Activity() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Activity getComplement() {
        return complement;
    }

    public void setComplement(Activity complement) {
        this.complement = complement;
    }

    public boolean hasComplement() {
        return complement.name != null && complement.type != null & complement.remarks != null;
    }

    public List<String> toCollection() {
        List<String> array = new ArrayList<>();
        array.add(name);
        array.add(type);
        array.add(remarks);
        return array;
    }

    @Override
    public String toString() {
        return "Activity{"
            + "name='" + name + '\''
            + ", type='" + type + '\''
            + ", remarks='" + remarks + '\''
            + ", complement=" + complement
            + '}';
    }
}
