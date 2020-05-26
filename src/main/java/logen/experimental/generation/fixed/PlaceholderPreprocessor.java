package logen.experimental.generation.fixed;

import logen.experimental.util.RandomChooser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A preprocessor that completes the construction of placeholders by
 * setting the number of logs that placeholders of type {@code FLEXIBLE}
 * specify.
 * <p>
 * The preprocessor has to be executed before the generation of filler logs.
 */
public class PlaceholderPreprocessor {
    /**
     * Sets the number of logs that a placeholder of type {@code FLEXIBLE}
     * specifies.
     * If the placeholder is not of type {@code FLEXIBLE}, no change will
     * be made to the placeholder.
     * @param globalLogCount The number of logs in the log sheet
     * @param placeholders The placeholders bundled in the fixture returned
     *                     from a {@code FixedLogGenerator}
     * @return The placeholders with their number of logs specified
     */
    public static List<Placeholder> preprocess(
            int globalLogCount,
            List<Placeholder.Builder> placeholders
    ) {
        List<Placeholder.Builder> anyTypePlaceholders = placeholders.stream()
                .filter(placeholder -> placeholder.getType().equals(PlaceholderType.FLEXIBLE))
                .collect(Collectors.toList());
        int placeholderCount = anyTypePlaceholders.size();

        int approxLogCount = globalLogCount / placeholderCount;
        List<Integer> approxLogCounts = new ArrayList<>(placeholderCount);
        int counter = 0;
        for (int i = 0; i < placeholderCount - 1; i++) {
            approxLogCounts.add(approxLogCount);
            counter += approxLogCount;
        }
        approxLogCounts.add(globalLogCount - counter);

        randomiseSizes(approxLogCounts);

        for (int i = 0; i < placeholderCount; i++) {
            anyTypePlaceholders.get(i).withLogCount(approxLogCounts.get(i));
        }

        return placeholders.stream()
                .map(Placeholder.Builder::build)
                .collect(Collectors.toList());
    }

    /**
     * Randomises the integers in a list while keeping the sum of
     * all the integers the same.
     * @param approxLogCounts A list of randomised integers.
     */
    private static void randomiseSizes(List<Integer> approxLogCounts) {
        int size = approxLogCounts.size();
        int from = 0;
        int to = 1;
        int counter = 1;
        while (counter != size) {
            int randomValue = RandomChooser.chooseBetween(1, approxLogCounts.get(from));

            int fromValue = approxLogCounts.get(from);
            int toValue = approxLogCounts.get(to);
            fromValue -= randomValue;
            toValue += randomValue;
            approxLogCounts.set(from, fromValue);
            approxLogCounts.set(to, toValue);

            from = (from + 1) % size;
            to = (to + 1) % size;
            counter++;
        }
    }
}
