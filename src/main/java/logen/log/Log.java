package logen.log;

import logen.scenario.common.LogSpec;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private final LocalTime time;
    private final String description;
    private final String type;
    private final String subject;
    private final String remark;

    public Log(LocalTime time, LogSpec spec) {
        this.time = time;
        this.description = spec.getDescription();
        this.type = spec.getType();
        this.subject = spec.getSubject();
        this.remark = spec.getRemark();
    }

    public LocalTime getTime() {
        return time;
    }

    public Object[] toArray() {
        List<String> array = new ArrayList<>();
        array.add(time.toString());
        array.add(description);
        array.add(type);
        array.add(subject);
        array.add(remark);
        return array.toArray();
    }
}
