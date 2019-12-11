package loggenerator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private LocalDateTime time;
    private Activity activity;
    private String subject;

    public Log(LocalDateTime time, Activity activity, String subject) {
        this.time = time;
        this.activity = activity;
        this.subject = subject;
    }

    public Object[] toArray() {
        List<String> array = new ArrayList<>();
        array.add(time.toString());
        array.addAll(activity.toCollection());
        array.add(subject);
        return array.toArray();
    }

    @Override
    public String toString() {
        return "Date Time: " + time + "\n"
                + "loggenerator.model.Activity: " + activity + "\n"
                + "Subject: " + subject + "\n";
    }
}
