package loggenerator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private LocalDateTime time;
    private Activity activity;
    private String subject;

    private Log() {

    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Log log;

        public Builder() {
            this.log = new Log();
        }

        public Builder withTime(LocalDateTime time) {
            log.time = time;
            return this;
        }

        public Builder withActivity(Activity activity) {
            log.activity = activity;
            return this;
        }

        public Builder withSubject(String subject) {
            log.subject = subject;
            return this;
        }

        public boolean hasComplement() {
            return log.activity.hasComplement();
        }

        public boolean isComplement() {
            return !hasComplement();
        }

        public Log build() {
            return log;
        }
    }
}
