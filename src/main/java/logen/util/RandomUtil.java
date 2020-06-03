package logen.util;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * Provides methods which produce non-deterministic outputs.
 */
public class RandomUtil {
    private static final Random rng;

    static {
        rng = new Random(System.currentTimeMillis());
    }

    private RandomUtil() {

    }

    /**
     * Randomly chooses an element from {@code values}.
     * {@code values} has to be non-empty.
     *
     * @param values A non-empty list
     * @param <E> The element type of {@code values}
     * @return A randomly chosen element from {@code values}
     */
    public static <E> E chooseFrom(List<E> values) {
        Objects.requireNonNull(values);
        if (values.isEmpty()) {
            throw new IllegalArgumentException();
        }

        int randomIndex = rng.nextInt(values.size());
        return values.get(randomIndex);
    }

    /**
     * Generates a random non-negative integer between two non-negative
     * integers {@code start} and {@code end} inclusive, where {@code start}
     * <= {@code end}.
     *
     * @param start The lower bound of the random non-negative integer
     * @param end The upper bound of the random non-negative integer
     * @return A random non-negative integer between {@code start} and
     *   {@code end} inclusive
     */
    public static int chooseBetweenInclusive(int start, int end) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException();
        }

        int delta = end - start;
        return start + rng.nextInt(delta + 1);
    }

    /**
     * Generates a random non-negative long between two non-negative
     * longs {@code start} and {@code end} inclusive, where {@code start}
     * <= {@code end}.
     *
     * @param start The lower bound of the random non-negative long
     * @param end The upper bound of the random non-negative long
     * @return A random non-negative long between {@code start} and
     *   {@code end} inclusive
     */
    public static long chooseBetweenInclusive(long start, long end) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException();
        }

        long delta = end - start;
        return start + rng.nextLong() % delta;
    }

    /**
     * Generates a random time value between {@code startTime} and
     * {@code endTime} inclusive, where {@code startTime} is before
     * or the same as {@code endTime}.
     *
     * @param startTime The earliest time value
     * @param endTime The latest time value
     * @return A random time value between {@code start} and {@code end}
     *   inclusive
     */
    public static LocalTime chooseBetweenInclusive(LocalTime startTime, LocalTime endTime) {
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException();
        }

        int startTimeSeconds = startTime.toSecondOfDay();
        int endTimeSeconds = endTime.toSecondOfDay();
        int chosenSeconds = chooseBetweenInclusive(startTimeSeconds, endTimeSeconds);
        return LocalTime.ofSecondOfDay(chosenSeconds);
    }

    /**
     * Randomises the distribution of quantifiable elements in
     * {@code values}.
     *
     * Distribution is done by choosing a quantity to be subtracted from
     * the quantity of an element using {@code choiceStrategy}, subtracting
     * that quantity from that element using {@code subtractionStrategy}
     * and adding it to the quantity of the next element using
     * {@code additionStrategy}.
     *
     * The distribution is done in just over one pass, with the last element
     * transferring a quantity to the first element.
     *
     * @param values The elements which quantity are to be distributed
     *               among each other
     * @param choiceStrategy The algorithm to choose a random quantity
     *                       from between the bounds inclusive of an
     *                       existing quantity of an element
     * @param subtractionStrategy The algorithm to subtract a quantity
     *                            from an element
     * @param additionStrategy The algorithm to add a quantity to an
     *                         element
     * @param <E> The element type of {@code values}
     * @return A randomised distribution of {@code values}
     */
    public static <E> List<E> randomise(
            List<E> values,
            BiFunction<Integer, E, E> choiceStrategy,
            BiFunction<E, E, E> subtractionStrategy,
            BiFunction<E, E, E> additionStrategy
    ) {
        int from = 0;
        int to = 1;
        int counter = 1;
        while (counter < values.size()) {
            E randomValue = choiceStrategy.apply(1, values.get(from));

            E fromValue = values.get(from);
            E toValue = values.get(to);
            fromValue = subtractionStrategy.apply(fromValue, randomValue);

            toValue = additionStrategy.apply(toValue, randomValue);
            values.set(from, fromValue);
            values.set(to, toValue);

            from = (from + 1) % 2;
            to = (to + 1) % 2;
            counter++;
        }
        return values;
    }
}
