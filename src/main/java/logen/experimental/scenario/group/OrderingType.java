package logen.experimental.scenario.group;

/**
 * The type of ordering in a group.
 */
public enum OrderingType {
    /**
     * The system determines the ordering of logs in the group.
     */
    ANY,
    /**
     * The user determines the ordering of logs in the group.
     */
    CUSTOM
}
