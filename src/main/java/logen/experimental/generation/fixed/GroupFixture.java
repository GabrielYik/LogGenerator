package logen.experimental.generation.fixed;

import logen.experimental.log.Log;

import java.util.List;

public class GroupFixture {
    private final List<Log> logs;
    private final List<Placeholder.Builder> placeholders;

    public GroupFixture(List<Log> logs, List<Placeholder.Builder> placeholders) {
        this.logs = logs;
        this.placeholders = placeholders;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public List<Placeholder.Builder> getPlaceholders() {
        return placeholders;
    }
}
