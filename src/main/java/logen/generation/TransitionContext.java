package logen.generation;

import java.util.List;
import logen.log.Log;

public class TransitionContext {
    private List<Log.Builder> partialLogs;

    public TransitionContext(List<Log.Builder> partialLogs) {
        this.partialLogs = partialLogs;
    }

    public List<Log.Builder> getPartialLogs() {
        return partialLogs;
    }
}
