package logen.experimental.generation.fixed;

import logen.experimental.log.Log;

import java.util.List;

/**
 * An aggregation that bundles the fixed logs and placeholders
 * generated from a {@code FixedLogGenerator}.
 */
public class Fixture {
    private final List<Log> logs;
    private final List<Placeholder> placeholders;

    /**
     * Constructs a fixture from {@code logs} and {@code placeholders}.
     * @param logs The fixed logs from a {@code FixedLogGenerator}
     * @param placeholders The placeholders from a {@code FixedLogGenerator}
     */
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
