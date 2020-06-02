package logen.util.timegenerators;

import java.time.LocalTime;

/**
 * A generator of time values.
 * This interface provides a standard way to invoke the generation
 * of time for all implementations.
 */
public interface TimeGenerator {
    /**
     * Generates a time value.
     *
     * @return A time value
     */
    LocalTime generate();
}
