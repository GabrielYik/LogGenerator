package logen.experimental.util;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class RandomChooser {
    private static final Random rng;

    static {
        rng = new Random(System.currentTimeMillis());
    }

    public static <E> E chooseFrom(List<E> collection) {
        int randomIndex = rng.nextInt(collection.size());
        return collection.get(randomIndex);
    }

    /**
     * Generates a random integer between {@code start} and
     * {@code end} inclusive.
     * @param start The minimum value.
     * @param end The maximum value.
     * @return A random integer between {@code start} and
     *   {@code end} inclusive.
     */
    public static int chooseBetween(int start, int end) {
        return start + rng.nextInt(end + 1);
    }

    public static LocalTime chooseBetween(LocalTime startTime, LocalTime endTime) {
        int startTimeSeconds = startTime.toSecondOfDay();
        int endTimeSeconds = endTime.toSecondOfDay();
        int deltaSeconds = endTimeSeconds - startTimeSeconds;
        return LocalTime.ofSecondOfDay(startTimeSeconds + deltaSeconds);
    }
}
