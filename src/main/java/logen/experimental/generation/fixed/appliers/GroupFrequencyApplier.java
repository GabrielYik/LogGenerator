package logen.experimental.generation.fixed.appliers;

import logen.experimental.generation.fixed.GroupFixture;
import logen.experimental.generation.fixed.Placeholder;
import logen.experimental.log.Log;
import logen.experimental.scenario.common.Frequency;

import java.util.ArrayList;
import java.util.List;

/**
 * An applier that acts on a group by applying the frequency attribute
 * defined on the group on logs in the group.
 */
public class GroupFrequencyApplier extends GroupAttributeApplier {
    private final Frequency frequency;

    private final List<Log> fixedLogs;
    private final List<Placeholder.Builder> placeholders;

    /**
     * Constructs a group frequency applier.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     * @param frequency The frequency defined on the logs in the group
     */
    public GroupFrequencyApplier(
            GroupFixture.Builder groupFixture,
            Frequency frequency
    ) {
        super(groupFixture);
        this.frequency = frequency;
        this.fixedLogs = groupFixture.getFixedLogs();
        this.placeholders = groupFixture.getPlaceholders();
    }

    @Override
    public GroupFixture.Builder apply() {
        if (doesNotRequireMultiplication()) {
            return groupFixture;
        }

        List<Log> multipliedFixedLogs = multiplyFixedLogs();
        List<Placeholder.Builder> multipliedPlaceholders = multiplyPlaceholders();
        return groupFixture
                .setFixedLogs(multipliedFixedLogs)
                .setPlaceholders(multipliedPlaceholders);
    }

    /**
     * Checks if the fixed logs and placeholders of the group
     * need to be multiplied with a factor corresponding to
     * the frequency count defined on the group.
     *
     * @return True if multiplication is required, false otherwise
     */
    private boolean doesNotRequireMultiplication() {
        return frequency.getCount() == 1;
    }

    /**
     * Multiplies the fixed logs by a factor corresponding to
     * the frequency count defined on the group.
     *
     * @return The fixed logs multiplied
     */
    private List<Log> multiplyFixedLogs() {
        List<Log> multipliedFixLogs = new ArrayList<>();
        for (int i = 0;i < frequency.getCount(); i++) {
            multipliedFixLogs.addAll(fixedLogs);
        }
        return multipliedFixLogs;
    }

    /**
     * Multiplies the placeholders by a factor corresponding to
     * the frequency count defined on the group.
     *
     * @return The placeholders multiplied
     */
    private List<Placeholder.Builder> multiplyPlaceholders() {
        List<Placeholder.Builder> multipliedPlaceholders = new ArrayList<>(placeholders);
        for (int i = 1; i < frequency.getCount(); i++) {
            int lastIndex = multipliedPlaceholders.size() - 1;
            Placeholder.Builder lastPlaceholder = multipliedPlaceholders.remove(lastIndex);
            List<Placeholder.Builder> nextPlaceholders = constructNextPlaceholders(lastPlaceholder);
            multipliedPlaceholders.addAll(nextPlaceholders);
        }
        return multipliedPlaceholders;
    }

    /**
     * Constructs the next placeholders by merging the last placeholder
     * of previous placeholders with the first placeholder of new
     * placeholders.
     *
     * @param earlierPlaceholder The last placeholder of previous placeholders
     * @return The next placeholders
     */
    private List<Placeholder.Builder> constructNextPlaceholders(Placeholder.Builder earlierPlaceholder) {
        List<Placeholder.Builder> copy = new ArrayList<>(placeholders);
        Placeholder.Builder laterPlaceholder = copy.get(0);
        Placeholder.Builder mergedPlaceholder = Placeholder.Builder.merge(earlierPlaceholder, laterPlaceholder);
        copy.set(0, mergedPlaceholder);
        return copy;
    }
}
