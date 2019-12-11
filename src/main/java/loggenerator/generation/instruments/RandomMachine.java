package loggenerator.generation.instruments;

import java.util.Random;

public class RandomMachine {
    protected Random rng;

    public RandomMachine() {
        rng = new Random(System.currentTimeMillis());
    }
}
