package logen.util;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class RandomUtil {
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

    public static long chooseBetween(long start, long end) {
        return start + rng.nextLong() % end;
    }

    public static LocalTime chooseBetween(LocalTime startTime, LocalTime endTime) {
        int startTimeSeconds = startTime.toSecondOfDay();
        int endTimeSeconds = endTime.toSecondOfDay();
        int deltaSeconds = endTimeSeconds - startTimeSeconds;
        return LocalTime.ofSecondOfDay(startTimeSeconds + deltaSeconds);
    }

    public static <E> List<E> randomise(
            List<E> distribution,
            BiFunction<Integer, E, E> choice,
            BiFunction<E, E, E> subtraction,
            BiFunction<E, E, E> addition
    ) {
        int from = 0;
        int to = 1;
        int counter = 1;
        while (counter != distribution.size()) {
            E randomValue = choice.apply(1, distribution.get(from));

            E fromValue = distribution.get(from);
            E toValue = distribution.get(to);
            fromValue = subtraction.apply(fromValue, randomValue);
            toValue = addition.apply(toValue, randomValue);
            distribution.set(from, fromValue);
            distribution.set(to, toValue);

            from = (from + 1) % 2;
            to = (to + 1) % 2;
            counter++;
        }
        return distribution;
    }
}
