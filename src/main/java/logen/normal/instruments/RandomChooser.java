package logen.normal.instruments;

import java.util.List;
import java.util.Random;

public class RandomChooser {
    private static Random rng;

    static {
        rng = new Random(System.currentTimeMillis());
    }

    public static <E> E chooseFrom(List<E> collection) {
         int randomIndex = rng.nextInt(collection.size());
         return collection.get(randomIndex);
    }

    public static int chooseFrom(int bound) {
        return rng.nextInt(bound);
    }
}
