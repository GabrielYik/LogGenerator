package loggenerator.suspicious;

import loggenerator.normal.instruments.RandomChooser;
import loggenerator.model.Log;

import java.util.ArrayList;
import java.util.List;

public class HighVolumeTroubleMaker implements TroubleMaker {
    private static final int COPY_COUNT = 10;

    private Log.Builder suspiciousLog;

    public HighVolumeTroubleMaker(Log.Builder suspiciousLog) {
        this.suspiciousLog = suspiciousLog;
    }

    @Override
    public List<Log.Builder> makeTrouble(List<Log.Builder> logs) {
        int jumpCount = RandomChooser.chooseFrom(logs.size());
        logs.addAll(jumpCount + 1, generateCopies(suspiciousLog));
        return logs;
    }

    private List<Log.Builder> generateCopies(Log.Builder log) {
        List<Log.Builder> copies = new ArrayList<>();
        for (int i = 0; i < COPY_COUNT; i++) {
            copies.add(log);
        }
        return copies;
    }
}
