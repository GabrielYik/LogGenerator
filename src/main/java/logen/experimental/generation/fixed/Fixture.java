package logen.experimental.generation.fixed;

import logen.experimental.log.Log;

import java.util.List;

/**
 * An aggregation that bundles the fixed logs and placeholders
 * generated from a {@code FixedLogGenerator}.
 */
public class Fixture {
    private final List<Log> fixedLogs;
    private final List<Placeholder> placeholders;

    /**
     * Constructs a fixture from {@code fixedLogs} and {@code placeholders}.
     * @param fixedLogs The fixed logs from a {@code FixedLogGenerator}
     * @param placeholders The placeholders from a {@code FixedLogGenerator}
     */
    public Fixture(List<Log> fixedLogs, List<Placeholder> placeholders) {
        this.fixedLogs = fixedLogs;
        this.placeholders = placeholders;
    }

    public List<Log> getFixedLogs() {
        return fixedLogs;
    }

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }
}
