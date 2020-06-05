package logen.generation.common;

/**
 * The type of a placeholder.
 */
public enum PlaceholderType {
    /**
     * The system determines the number of logs to be generated
     * in the space represented by the placeholder.
     */
    FLEXIBLE,
    /**
     * The user determines the number of logs to be generated
     * in the space represented by the placeholder.
     */
    CUSTOM
}
