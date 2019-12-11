package loggenerator.generation;

import loggenerator.model.Log;
import loggenerator.generation.instruments.activity.ActivityGenerator;
import loggenerator.generation.instruments.subject.SubjectGenerator;
import loggenerator.generation.instruments.time.TimeGenerator;
import loggenerator.model.Activity;

import java.time.LocalDateTime;

public class InstrumentConductor {
    private int currentLogCount;
    private int finalLogCount;
    private TimeGenerator timeGenerator;
    private ActivityGenerator activityGenerator;
    private SubjectGenerator subjectGenerator;

    public InstrumentConductor(
            int finalLogCount,
            TimeGenerator timeGenerator,
            ActivityGenerator activityGenerator,
            SubjectGenerator subjectGenerator) {
        currentLogCount = 0;
        this.finalLogCount = finalLogCount;
        this.timeGenerator = timeGenerator;
        this.activityGenerator = activityGenerator;
        this.subjectGenerator = subjectGenerator;
    }

    public Log orchestrate() {
        LocalDateTime dateTime = timeGenerator.generate();
        Activity activity = activityGenerator.generate(currentLogCount, finalLogCount);
        String subject = subjectGenerator.generate();
        Log log = new Log(dateTime, activity, subject, 0, "");
        currentLogCount++;
        return log;
    }

    public boolean isDone() {
        return currentLogCount == finalLogCount;
    }
}
