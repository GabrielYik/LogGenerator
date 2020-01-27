package logen.storage;

import java.time.LocalTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Period {
    private Active active;
    private Inactive inactive;

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public Inactive getInactive() {
        return inactive;
    }

    public void setInactive(Inactive inactive) {
        this.inactive = inactive;
    }

    public static class Active {
        private String days;
        @JsonDeserialize(using = LocalTimeDeserialiser.class)
        private LocalTime startTime;
        @JsonDeserialize(using = LocalTimeDeserialiser.class)
        private LocalTime endTime;

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }
    }

    public static class Inactive {
        private String days;

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }
    }
}
