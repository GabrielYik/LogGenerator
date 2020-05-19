package logen.log;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String description;
    private String type;
    private String subject;
    private String remarks;
    private Activity complement;
    private String persistence;

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

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public boolean isPersistent() {
        return persistence == null;
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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Activity)) {
            return false;
        }

        Activity activity = (Activity) other;
        return description.equals(activity.description)
            && type.equals(activity.type)
            && subject.equals(activity.subject)
            && remarks.equals(activity.remarks)
            && persistence.equals(activity.persistence);
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
