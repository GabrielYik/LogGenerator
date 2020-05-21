package logen.experimental.generation.fixed;

import logen.experimental.log.Log;

import java.util.List;

public class Fixture {
    private final List<Log> logs;
    private final List<Placeholder> placeholders;

    public Fixture(List<Log> logs, List<Placeholder> placeholders) {
        this.logs = logs;
        this.placeholders = placeholders;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }
}
