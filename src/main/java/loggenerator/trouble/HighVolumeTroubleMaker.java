package loggenerator.trouble;

import loggenerator.generation.instruments.RandomMachine;
import loggenerator.model.Log;

import java.util.ArrayList;
import java.util.List;

public class HighVolumeTroubleMaker extends RandomMachine implements TroubleMaker {
    private static final int COPY_COUNT = 10;

    @Override
    public List<Log> makeTrouble(List<Log> logs) {
        int jumpCount = choose(logs.size());
        Log chosenLog = logs.get(jumpCount);
        logs.addAll(jumpCount + 1, generateCopies(chosenLog));
        return logs;
    }

    private int choose(int bound) {
        return rng.nextInt(bound);
    }

    private List<Log> generateCopies(Log log) {
        List<Log> copies = new ArrayList<>();
        for (int i = 0; i < COPY_COUNT; i++) {
            copies.add(log);
        }
        return copies;
    }
}
