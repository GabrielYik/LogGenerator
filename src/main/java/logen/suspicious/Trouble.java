package logen.suspicious;

import java.time.LocalTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import logen.storage.LocalTimeDeserialiser;

public class Trouble {
    private String type;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime time;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime nextTime;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getNextTime() {
        return nextTime;
    }

    public void setNextTime(LocalTime nextTime) {
        this.nextTime = nextTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
