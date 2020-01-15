package logen.log;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Activity complement;

    public Activity() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean hasSubject() {
        return subject != null;
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
        return complement != null;
    }

    public List<String> toCollection() {
        List<String> array = new ArrayList<>();
        array.add(description);
        array.add(type);
        array.add(subject);
        array.add(remarks);
        return array;
    }

    @Override
    public String toString() {
        return "Activity{"
            + "name='" + description + '\''
            + ", type='" + type + '\''
            + ", remarks='" + remarks + '\''
            + ", complement=" + complement
            + '}';
    }
}
