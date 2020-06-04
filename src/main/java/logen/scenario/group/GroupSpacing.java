package logen.scenario.group;

import java.util.ArrayList;
import java.util.List;

public class GroupSpacing {
    private static final GroupSpacingType DEFAULT_TYPE = GroupSpacingType.ANY;
    private static final int DEFAULT_AMOUNT = -1;

    private GroupSpacingType type;
    private List<Integer> amounts;

    /**
     * Sets the attribute or sub-attributes of {@code spacing} if any
     * are missing.
     *
     * At this point, there are 3 cases of missing sub-attributes that can
     * be handled:
     * <ol>
     *     <li>Type and amounts
     *     <li>Type
     *     <li>Given type of {@code ANY}, amounts
     * </ol>
     *
     * @param spacing The attribute
     * @param logSpecCount The number of log specifications for computation
     *                     of default amounts
     */
    public static void setAttributesIfAbsent(GroupSpacing spacing, int logSpecCount) {
        if (spacing == null) {
            spacing = new GroupSpacing();
        }

        if (spacing.type == null && spacing.amounts == null) {
            spacing.type = DEFAULT_TYPE;
            spacing.amounts = computeDefaultAmounts(logSpecCount);
            return;
        }

        if (spacing.type == null && spacing.amounts != null) {
            spacing.type = GroupSpacingType.CUSTOM;
            return;
        }

        if (spacing.type == DEFAULT_TYPE && spacing.amounts == null) {
            spacing.amounts = computeDefaultAmounts(logSpecCount);
        }
    }

    /**
     * Computes a list of size {@code logSpecCount} - 1 filled with values
     * of no semantic value.
     * The size is {@code logSpecCount} - 1 since for n log specifications,
     * there are only n - 1 spaces in between.
     *
     * This computation is done to ensure consistency with the way that
     * {@link GroupOrdering} is handled by
     * {@link logen.generation.fixed.appliers.GroupOrderingApplier}.
     *
     * @param logSpecCount The number of log specifications
     * @return A list of size {@code logSpecCount} of no semantic value
     */
    private static List<Integer> computeDefaultAmounts(int logSpecCount) {
        List<Integer> amounts = new ArrayList<>(logSpecCount - 1);
        for (int i = 0; i < logSpecCount - 1; i++) {
            amounts.add(DEFAULT_AMOUNT);
        }
        return amounts;
    }

    public GroupSpacingType getType() {
        return type;
    }

    public void setType(GroupSpacingType type) {
        this.type = type;
    }

    public List<Integer> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Integer> amounts) {
        this.amounts = amounts;
    }
}
