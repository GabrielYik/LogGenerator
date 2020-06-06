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

    public static void requireNonNegative(int... integers) {
        for (int integer : integers) {
            if (integer < 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Verifies that all time values in {@code timeValues} are
     * consecutively equal or after each other.
     *
     * @param timeValues The time values to verify
     * @throws IllegalArgumentException if any of the time values
     *   are not consecutively after each other
     */
    public static void requireInOrder(LocalTime... timeValues) {
        for (int i = 0; i < timeValues.length - 1; i++) {
            if (timeValues[i].isAfter(timeValues[i + 1])) {
                throw new IllegalArgumentException();
            }
        }
    }
}
