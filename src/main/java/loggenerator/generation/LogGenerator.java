package loggenerator.generation;

import loggenerator.model.Log;
import loggenerator.Scenario;
import loggenerator.generation.instruments.activity.ActivityGenerator;
import loggenerator.generation.instruments.activity.ActivityGeneratorFactory;
import loggenerator.generation.instruments.subject.SubjectGenerator;
import loggenerator.generation.instruments.subject.SubjectGeneratorFactory;
import loggenerator.generation.instruments.time.TimeGenerator;
import loggenerator.generation.instruments.time.TimeGeneratorFactory;

import java.util.LinkedList;
import java.util.List;

public class LogGenerator {
    private InstrumentConductor conductor;

    public LogGenerator(Scenario scenario) {
        int logCount = scenario.getLogCount();
        TimeGenerator timeGenerator = TimeGeneratorFactory.getTimeGenerator(
                scenario.getTimeGeneratorType());
        ActivityGenerator activityGenerator = ActivityGeneratorFactory.getActivityGenerator(
                scenario.getActivityGeneratorType(), scenario.getActivities());
        SubjectGenerator subjectGenerator = SubjectGeneratorFactory.getSubjectGenerator(
                scenario.getSubjectGeneratorType(), scenario.getSubjects());
        conductor = new InstrumentConductor(logCount, timeGenerator, activityGenerator, subjectGenerator);
    }

    public List<Log> generate() {
        List<Log> randomLogs = generateRandomLogs();
        return injectSuspiciousLogs(randomLogs);
    }

    private List<Log> generateRandomLogs() {
        List<Log> logs = new LinkedList<>();
        boolean isSatisfied = false;
        while (!isSatisfied) {
            Log log = conductor.orchestrate();
            logs.add(log);
            if (conductor.isDone()) {
                isSatisfied = true;
            }
        }
        return logs;
    }

    private List<Log> injectSuspiciousLogs(List<Log> randomLogs) {
        return randomLogs;
    }
}
