package logen.experimental.generation.fixed;

import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;

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

    public static class Builder {
        private List<LogSpec> logSpecs;
        private List<Log> fixedLogs;
        private List<Placeholder.Builder> placeholders;

        public Builder() {

        }

        public Builder setLogSpecs(List<LogSpec> logSpecs) {
            this.logSpecs = logSpecs;
            return this;
        }

        public Builder setFixedLogs(List<Log> fixedLogs) {
            this.fixedLogs = fixedLogs;
            return this;
        }

        public Builder setPlaceholders(List<Placeholder.Builder> placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public GroupFixture build() {
            return new GroupFixture(fixedLogs, placeholders);
        }

        public List<LogSpec> getLogSpecs() {
            return logSpecs;
        }

        public List<Log> getFixedLogs() {
            return fixedLogs;
        }

        public List<Placeholder.Builder> getPlaceholders() {
            return placeholders;
        }
    }
}
