package logen.experimental.generation.fixed;

import logen.experimental.log.Log;
import logen.experimental.scenario.common.LogSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * An aggregation that bundles the fixed logs and incomplete
 * placeholders generated from a {@code GroupProcessor}.
 */
public class GroupFixture {
    private final List<Log> logs;
    private final List<Placeholder.Builder> placeholders;

    private GroupFixture() {
        logs = new ArrayList<>();
        placeholders = new ArrayList<>();
    }

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

    public static GroupFixture empty() {
        return new GroupFixture();
    }

    public static GroupFixture merge(GroupFixture first, GroupFixture second) {
        List<Log> consolidatedLogs = new ArrayList<>(first.logs);
        consolidatedLogs.addAll(second.logs);
        List<Placeholder.Builder> consolidatedPlaceholders = Placeholder.Builder.merge(
                first.placeholders,
                second.placeholders
        );
        return new GroupFixture(consolidatedLogs, consolidatedPlaceholders);
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

        public Builder copy() {
            return new Builder()
                    .setLogSpecs(logSpecs)
                    .setFixedLogs(fixedLogs)
                    .setPlaceholders(placeholders);
        }

        public static Builder empty() {
            return new Builder();
        }

        public static Builder merge(Builder first, Builder second) {
            List<Log> consolidatedLogs = new ArrayList<>(first.fixedLogs);
            consolidatedLogs.addAll(second.fixedLogs);
            List<Placeholder.Builder> consolidatedPlaceholders = Placeholder.Builder.merge(
                    first.placeholders,
                    second.placeholders
            );
            return new GroupFixture.Builder()
                    .setLogSpecs(first.logSpecs)
                    .setFixedLogs(consolidatedLogs)
                    .setPlaceholders(consolidatedPlaceholders);
        }
    }
}
