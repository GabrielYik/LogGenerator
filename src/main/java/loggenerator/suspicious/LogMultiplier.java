package loggenerator.suspicious;

import loggenerator.normal.instruments.RandomChooser;
import loggenerator.model.Log;
import loggenerator.normal.instruments.SimpleTimeGenerator;

import java.util.ArrayList;
import java.util.List;

public class LogMultiplier implements TroubleMaker {
    private static final int COPY_COUNT = 10;

    private Log.Builder suspiciousLog;

    public LogMultiplier(Log.Builder suspiciousLog) {
        this.suspiciousLog = suspiciousLog;
    }

    @Override
    public List<Log.Builder> makeTrouble(List<Log.Builder> logs) {
        int insertionPoint = logs.size();
        logs.addAll(insertionPoint, generateCopies(suspiciousLog));
        return logs;
    }

    private List<Log.Builder> generateCopies(Log.Builder log) {
        List<Log.Builder> copies = new ArrayList<>();
        for (int i = 0; i < COPY_COUNT; i++) {
            Log.Builder copy = (Log.Builder) log.clone();
            copy.withTime(SimpleTimeGenerator.generate());
            copies.add(copy);
        }
        return copies;
    }
}
