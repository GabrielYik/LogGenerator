package loggenerator.generation.instruments.activity;

import loggenerator.model.Activity;

public interface ActivityGenerator {
    Activity generate(int currentLogCount, int finalLogCount);
}
