package loggenerator.generation.instruments;

import java.util.Random;

public abstract class AbstractGenerator {
    protected Random rng;

    public AbstractGenerator() {
        rng = new Random(System.currentTimeMillis());
    }
}
