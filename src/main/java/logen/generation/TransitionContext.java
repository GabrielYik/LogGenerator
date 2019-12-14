package logen.generation;

import java.time.LocalTime;
import java.util.List;
import logen.log.Log;

public class TransitionContext {
    private List<Log.Builder> partialLogs;
    private LocalTime previousTime;

    public TransitionContext(List<Log.Builder> partialLogs, LocalTime previousTime) {
        this.partialLogs = partialLogs;
        this.previousTime = previousTime;
    }

    public List<Log.Builder> getPartialLogs() {
        return partialLogs;
    }

    public LocalTime getPreviousTime() {
        return previousTime;
    }
}
