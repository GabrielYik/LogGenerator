package loggenerator.model;

import loggenerator.model.Activity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private LocalDateTime time;
    private Activity activity;
    private String subject;
    private int severity;
    private String remarks;

    public Log(LocalDateTime time, Activity activity, String subject, int severity, String remarks) {
        this.time = time;
        this.activity = activity;
        this.subject = subject;
        this.severity = severity;
        this.remarks = remarks;
    }

    public Object[] toArray() {
        List<String> array = new ArrayList<>();
        array.add(time.toString());
        array.addAll(activity.toCollection());
        array.add(String.valueOf(severity));
        array.add(subject);
        array.add(remarks);
        return array.toArray();
    }

    @Override
    public String toString() {
        return "Date Time: " + time + "\n"
                + "loggenerator.model.Activity: " + activity + "\n"
                + "Severity: " + severity + "\n"
                + "Subject: " + subject + "\n"
                + "Remarks: " + remarks;
    }
}
