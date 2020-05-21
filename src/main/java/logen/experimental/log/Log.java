package logen.experimental.log;

import logen.experimental.scenario.LogSpec;

import java.time.LocalTime;

public class Log {
    private final LocalTime time;
    private final String description;
    private final String type;
    private final String subject;
    private final String remarks;

    public Log(LocalTime time, LogSpec spec) {
        this.time = time;
        this.description = spec.getDescription();
        this.type = spec.getType();
        this.subject = spec.getSubject();
        this.remarks = spec.getSubject();
    }
}
