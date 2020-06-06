package logen.util;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Provides methods for checking specific conditions on method
 * arguments.
 */
public class Validations {
    /**
     * Requires that all the objects in {@code objects} are not
     * null.
     *
     * @param objects The objects to be checked
     * @param <E> The element type of {@code objects}
     */
    @SafeVarargs
    public static <E> void requireNonNull(E... objects) {
        for (E object : objects) {
            Objects.requireNonNull(object);
        }
    }

    /**
     * Verifies that all time points in {@code timePoints} are
     * consecutively equal or after each other.
     *
     * @param timePoints The time points to verify
     * @throws IllegalArgumentException if any of the time points
     *   are not consecutively after each other
     */
    public static void requireInOrder(LocalTime... timePoints) {
        for (int i = 0; i < timePoints.length - 1; i++) {
            if (timePoints[i].isAfter(timePoints[i + 1])) {
                throw new IllegalArgumentException();
            }
        }
    }
}
