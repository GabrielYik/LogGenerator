package loggenerator.suspicious;

import loggenerator.model.Activity;
import loggenerator.model.Log;

public class TroubleMakingContext {
    private String type;
    private Log.Builder suspiciousLog;
    private String subject;

    public TroubleMakingContext(String type, Log.Builder suspiciousLog, String subject) {
        this.type = type;
        this.suspiciousLog = suspiciousLog;
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public Log.Builder getSuspiciousLog() {
        return suspiciousLog;
    }

    public String getSubject() {
        return subject;
    }
}
