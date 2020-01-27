package logen.generation.suspicious;

import java.time.LocalTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import logen.storage.LocalTimeDeserialiser;

public class Trouble {
    private String type;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime endTime;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
