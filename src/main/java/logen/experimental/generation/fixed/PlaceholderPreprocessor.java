package logen.experimental.generation.fixed;

import logen.experimental.scenario.group.SpaceType;
import logen.experimental.util.RandomChooser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderPreprocessor {
    public static List<Placeholder> preprocess(
            int logCount,
            List<Placeholder.Builder> placeholders
    ) {
        List<Placeholder.Builder> anyTypePlaceholders = placeholders.stream()
                .filter(placeholder -> placeholder.getSpaceType().equals(SpaceType.ANY))
                .collect(Collectors.toList());
        int placeholderCount = anyTypePlaceholders.size();

        int approxLogCount = logCount / placeholderCount;
        List<Integer> approxLogCounts = new ArrayList<>(placeholderCount);
        int counter = 0;
        for (int i = 0; i < placeholderCount - 1; i++) {
            approxLogCounts.add(approxLogCount);
            counter += approxLogCount;
        }
        approxLogCounts.add(logCount - counter);

        randomiseSizes(approxLogCounts);

        for (int i = 0; i < placeholderCount; i++) {
            anyTypePlaceholders.get(i).withSpaceAmount(approxLogCounts.get(i));
        }

        return placeholders.stream()
                .map(Placeholder.Builder::build)
                .collect(Collectors.toList());
    }

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
