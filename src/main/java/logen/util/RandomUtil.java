package logen.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
     * @throws NullPointerException if {@code values} is null
     * @throws IllegalArgumentException if {@code values} is empty
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
     * @throws IllegalArgumentException if {@code start} or {@code end} is
     *   negative, or {@code start} > {@code end}
     */
    public static int chooseBetweenInclusive(int start, int end) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException();
        }

        if (start == end) {
            return start;
        }

        int delta = end - start;
        return start + rng.nextInt(delta + 1);
    }

    /**
     * A variant of {@link RandomUtil#chooseBetweenInclusive(int, int)}
     * for longs.
     */
    public static long chooseBetweenInclusive(long start, long end) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException();
        }

        if (start == end) {
            return start;
        }

        long delta = end - start;
        return start + Math.abs(rng.nextLong()) % delta;
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
     * @throws NullPointerException if either {@code startTime} or
     *   {@code endTime} is null
     * @throws IllegalArgumentException if {@code startTime} is after
     *   {@code endTime}
     */
    public static LocalTime chooseBetweenInclusive(
            LocalTime startTime,
            LocalTime endTime
    ) {
        Validations.requireNonNull(startTime, endTime);
        Validations.requireInOrder(startTime, endTime);

        int startTimeSeconds = startTime.toSecondOfDay();
        int endTimeSeconds = endTime.toSecondOfDay();
        int chosenSeconds = chooseBetweenInclusive(startTimeSeconds, endTimeSeconds);
        return LocalTime.ofSecondOfDay(chosenSeconds);
    }

    /**
     * Randomly distributes the integer quantity of {@code sum} among the
     * positions of a list of size {@code count}.
     *
     * @param sum The quantity to be distributed
     * @param count The size of the output list
     * @return A list of size {@code count} with a random distribution
     *   of the quantity {@code sum} distributed among its positions
     * @throws IllegalArgumentException if either {@code sum} or {@code count}
     *   is negative
     */
    public static List<Integer> distributeRandomly(int sum, int count) {
        Validations.requireNonNegative(sum, count);

        if (sum == 0) {
            return Collections.nCopies(count, 0);
        }
        if (count == 0) {
            return Collections.emptyList();
        }

        List<Integer> values = distributeUniformly(sum, count);
        return distributeRandomly(
                values,
                RandomUtil::chooseBetweenInclusive,
                (a, b) -> a - b,
                Integer::sum
        );
    }

    /**
     * A variant of {@link this#distributeRandomly(int, int)} for a long
     * {@code sum}.
     */
    public static List<Long> distributeRandomly(long sum, int count) {
        if (sum < 0L || count < 0) {
            throw new IllegalArgumentException();
        }

        if (sum == 0L) {
            return new ArrayList<>(Collections.nCopies(count, 0L));
        }
        if (count == 0L) {
            return Collections.emptyList();
        }

        List<Long> values = distributeUniformly(sum, count);
        return distributeRandomly(
                values,
                RandomUtil::chooseBetweenInclusive,
                (a, b) -> a - b,
                Long::sum
        );
    }

    /**
     * Randomises the distribution of quantifiable elements in
     * {@code values}.
     *
     * For this method to cater to different instances of the element
     * type {@code E}, the operations involving choosing an element
     * from a list, subtracting one value from another, and adding two
     * values together are abstracted as instances of {@code BiFunction}.
     *
     * @param values The elements which quantity are to be distributed
     *               among each other
     * @param choiceStrategy The algorithm to choose a random quantity
     *                       from between the bounds inclusive of an
     *                       existing quantity of an element
     * @param subtractionStrategy The algorithm to subtract a value
     *                            from another
     * @param additionStrategy The algorithm to add two values together
     * @param <E> The element type of {@code values}
     * @return A randomised distribution of {@code values}
     */
    private static <E> List<E> distributeRandomly(
            List<E> values,
            BiFunction<Integer, E, E> choiceStrategy,
            BiFunction<E, E, E> subtractionStrategy,
            BiFunction<E, E, E> additionStrategy
    ) {
        int from = 0;
        int to = 1;
        int counter = 1;
        while (counter < values.size()) {
            E randomValue = choiceStrategy.apply(0, values.get(from));

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
        Collections.shuffle(values);
        return values;
    }

    /**
     * A wrapper around {@link this#distributeUniformly(Object, int, Object, BiFunction, BiFunction, BiFunction)}
     * for {@code sum} of type integer.
     */
    private static List<Integer> distributeUniformly(int sum, int count) {
        return distributeUniformly(
                sum,
                count,
                0,
                (a, b) -> a / b,
                Integer::sum,
                (a, b) -> a - b
        );
    }

    /**
     * A wrapper around {@link this#distributeUniformly(Object, int, Object, BiFunction, BiFunction, BiFunction)}
     * for {@code sum} of type long.
     */
    private static List<Long> distributeUniformly(long sum, int count) {
        return distributeUniformly(
                sum,
                count,
                0L,
                (a, b) -> a / b,
                Long::sum,
                (a, b) -> a - b
        );
    }

    /**
     * Uniformly distributes the quantity of {@code sum} among the
     * positions of a list of size {@code count}.
     *
     * In the event that sum % count != 0, the resulting distribution
     * will be close to uniform.
     *
     * @param sum The quantity to be distributed
     * @param count The size of the output list
     * @param initialValue The equivalent of the number 0 for the
     *                     type of {@code E}
     * @param divisionStrategy The algorithm to divide one value from
     *                         another
     * @param additionStrategy The algorithm to add two values together
     * @param subtractionStrategy The algorithm to subtract a value from
     *                            another
     * @param <E> The element type of {@code sum}
     * @return A list of size {@code count} with a uniform distribution
     *   of the quantity {@code sum} distributed among its positions
     */
    private static <E> List<E> distributeUniformly(
            E sum,
            int count,
            E initialValue,
            BiFunction<E, Integer, E> divisionStrategy,
            BiFunction<E, E, E> additionStrategy,
            BiFunction<E, E, E> subtractionStrategy
    ) {
        List<E> values = new ArrayList<>(count);
        E value = divisionStrategy.apply(sum, count);
        E sumUsed = initialValue;
        for (int i = 0; i < count - 1; i++) {
            values.add(value);
            sumUsed = additionStrategy.apply(sumUsed, value);
        }
        E remainder = subtractionStrategy.apply(sum, sumUsed);
        values.add(remainder);
        Collections.shuffle(values);
        return values;
    }
}
