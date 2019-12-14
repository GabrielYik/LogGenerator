package logen.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Log implements Cloneable {
    private LocalTime time;
    private Activity activity;
    private String subject;

    private Log() {

    }

    public Log(LocalTime time, Activity activity, String subject) {
        this.time = time;
        this.activity = activity;
        this.subject = subject;
    }

    /**
     *
     * @return
     */
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

    @Override
    public Object clone() {
        Log clone = new Log();
        clone.time = LocalTime.now();
        clone.activity = activity;
        clone.subject = subject;
        return clone;
    }

    /**
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Cloneable {
        private Log log;

        public Builder() {
            log = new Log();
        }

        public Builder withTime(LocalTime time) {
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

        @Override
        public Object clone() {
            Builder clone = new Builder();
            clone.log = (Log) log.clone();
            return clone;
        }

        public Log build() {
            return log;
        }

        public LocalTime getTime() {
            return log.time;
        }
    }
}
