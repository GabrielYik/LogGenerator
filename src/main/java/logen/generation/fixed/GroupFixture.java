package logen.generation.fixed;

import logen.log.Log;
import logen.scenario.common.LogSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * An aggregation that bundles the fixed logs and incomplete
 * placeholders generated from a {@code GroupProcessor}.
 */
public class GroupFixture {
    private final List<Log> fixedLogs;
    private final List<Placeholder.Builder> placeholders;

    private GroupFixture() {
        fixedLogs = new ArrayList<>();
        placeholders = new ArrayList<>();
    }

    /**
     * Constructs a group fixture from {@code fixedLogs} and {@code placeholders}.
     * @param fixedLogs The fixed logs from a {@code GroupProcessor}
     * @param placeholders The incomplete placeholders from a {@code GroupProcessor}
     */
    public GroupFixture(List<Log> fixedLogs, List<Placeholder.Builder> placeholders) {
        this.fixedLogs = fixedLogs;
        this.placeholders = placeholders;
    }

    public List<Log> getFixedLogs() {
        return fixedLogs;
    }

    public List<Placeholder.Builder> getPlaceholders() {
        return placeholders;
    }

    public static GroupFixture empty() {
        return new GroupFixture();
    }

    public static GroupFixture merge(GroupFixture first, GroupFixture second) {
        List<Log> consolidatedFixedLogs = new ArrayList<>(first.fixedLogs);
        consolidatedFixedLogs.addAll(second.fixedLogs);
        List<Placeholder.Builder> consolidatedPlaceholders = Placeholder.Builder.merge(
                first.placeholders,
                second.placeholders
        );
        return new GroupFixture(consolidatedFixedLogs, consolidatedPlaceholders);
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
            List<Log> consolidatedFixedLogs = new ArrayList<>(first.fixedLogs);
            consolidatedFixedLogs.addAll(second.fixedLogs);
            List<Placeholder.Builder> consolidatedPlaceholders = Placeholder.Builder.merge(
                    first.placeholders,
                    second.placeholders
            );
            return new GroupFixture.Builder()
                    .setLogSpecs(first.logSpecs)
                    .setFixedLogs(consolidatedFixedLogs)
                    .setPlaceholders(consolidatedPlaceholders);
        }
    }
}
