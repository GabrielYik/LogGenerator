package logen.generation.fixed.appliers;

import logen.generation.fixed.GroupFixture;
import logen.generation.fixed.Placeholder;
import logen.generation.fixed.PlaceholderType;
import logen.scenario.group.GroupSpacing;

import java.util.ArrayList;
import java.util.List;

/**
 * An applier that acts on a group by applying the spacing attribute
 * defined on the group onto the logs in the group.
 */
public class GroupSpacingApplier extends GroupAttributeApplier {
    private final GroupSpacing spacing;

    /**
     * Constructs a group spacing applier.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     * @param spacing The spacing defined on the logs in the group
     */
    public GroupSpacingApplier(GroupFixture.Builder groupFixture, GroupSpacing spacing) {
        super(groupFixture);
        this.spacing = spacing;
    }

    @Override
    public GroupFixture.Builder apply() {
        List<Placeholder.Builder> placeholders;
        switch (spacing.getType()) {
            case ANY:
                placeholders = generateFlexiblePlaceholders();
                break;
            case CUSTOM:
                placeholders = generateCustomPlaceholders();
                break;
            default:
                throw new AssertionError();
        }
        insertPaddingPlaceholders(placeholders);
        return groupFixture.setPlaceholders(placeholders);
    }

    /**
     * Generates placeholders of type {@code FLEXIBLE} for spacing of
     * type {@code ANY}.
     * No log count is defined for the placeholders since not enough
     * information is available at this point to make that choice.
     *
     * @return Placeholders of type {@code FLEXIBLE}
     */
    private List<Placeholder.Builder> generateFlexiblePlaceholders() {
        List<Placeholder.Builder> placeholders = new ArrayList<>();
        for (int i = 0; i < spacing.getAmounts().size(); i++) {
            Placeholder.Builder placeholder = new Placeholder.Builder()
                    .withType(PlaceholderType.FLEXIBLE);
            placeholders.add(placeholder);
        }
        return placeholders;
    }

    /**
     * Generates placeholders of type {@code CUSTOM} for spacing of type
     * {@code CUSTOM}.
     *
     * @return Placeholders of type {@code CUSTOM}
     */
    private List<Placeholder.Builder> generateCustomPlaceholders() {
        List<Placeholder.Builder> placeholders = new ArrayList<>();
        for (int amount : spacing.getAmounts()) {
            Placeholder.Builder placeholder = new Placeholder.Builder()
                    .withType(PlaceholderType.CUSTOM)
                    .withLogCount(amount);
            placeholders.add(placeholder);
        }
        return placeholders;
    }

    /**
     * Inserts one placeholder at the front of {@code placeholders} and
     * another at the back of {@code placeholders}.
     *
     * @param placeholders The placeholders to insert padding placeholders
     *                     to
     */
    private void insertPaddingPlaceholders(List<Placeholder.Builder> placeholders) {
        Placeholder.Builder firstPlaceholder = new Placeholder.Builder()
                .withType(PlaceholderType.FLEXIBLE);
        Placeholder.Builder lastPlaceholder = new Placeholder.Builder()
                .withType(PlaceholderType.FLEXIBLE);
        placeholders.add(0, firstPlaceholder);
        placeholders.add(lastPlaceholder);
    }
}
