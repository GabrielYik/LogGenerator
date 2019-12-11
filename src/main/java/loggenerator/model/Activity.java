package loggenerator.model;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String name;
    private String type;
    private String complement;

    public Activity() {

    }

    public Activity(String name, String type) {
        this.name = name;
        this.type = type;
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

    public List<String> toCollection() {
        List<String> array = new ArrayList<>();
        array.add(name);
        array.add(type);
        return array;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public Activity complement() {
        return new Activity(complement, type);
    }

    @Override
    public String toString() {
        return "loggenerator.model.Activity{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
