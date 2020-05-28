package logen.experimental.generation.fixed.appliers;

import logen.experimental.generation.fixed.GroupFixture;

/**
 * An applier that acts on a group by applying an attribute defined
 * on the group onto the logs in the group.
 */
public abstract class GroupAttributeApplier {
    /**
     * The container passed between group attribute appliers.
     * Data can be added or updated as required.
     */
    protected final GroupFixture.Builder groupFixture;

    /**
     * Constructs a group attribute applier from {@code groupFixture}.
     * {@code groupFixture} may be a fresh object or carry existing
     * context.
     *
     * @param groupFixture The container passed between group attribute
     *                     appliers
     */
    protected GroupAttributeApplier(GroupFixture.Builder groupFixture) {
        this.groupFixture = groupFixture;
    }

    /**
     * Applies the group attribute onto the logs in the group.
     *
     * @return An intermediate group fixture containing the results
     *   of the application
     */
    public abstract GroupFixture.Builder apply();
}
