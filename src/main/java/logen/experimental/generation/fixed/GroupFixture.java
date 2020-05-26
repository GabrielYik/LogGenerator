package logen.experimental.generation.fixed;

import logen.experimental.log.Log;

import java.util.List;

/**
 * An aggregation that bundles the fixed logs and incomplete
 * placeholders generated from a {@code GroupProcessor}.
 */
public class GroupFixture {
    private final List<Log> logs;
    private final List<Placeholder.Builder> placeholders;

    /**
     * Constructs a group fixture from {@code logs} and {@code placeholders}.
     * @param logs The fixed logs from a {@code GroupProcessor}
     * @param placeholders The incomplete placeholders from a {@code GroupProcessor}
     */
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
