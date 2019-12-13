package loggenerator.normal;

import loggenerator.normal.instruments.RandomChooser;
import loggenerator.model.Log;
import loggenerator.model.Activity;
import loggenerator.normal.instruments.SimpleTimeGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class InstrumentConductor {
    private static final int CHOICE_COUNT = 2;

    private List<Activity> activities;
    private List<String> subjects;
    private List<Log.Builder> partialLogs;
    private Stack<Log.Builder> complementPartialLogs;
    private List<Supplier<Log.Builder>> choices;

    public InstrumentConductor(List<Activity> activities, List<String> subjects) {
        this.activities = activities;
        this.subjects = subjects;

        partialLogs = new LinkedList<>();
        complementPartialLogs = new Stack<>();
        choices = new ArrayList<>();
        choices.add(this::createFreshPartialLog);
        choices.add(this::chooseComplementPartialLog);
    }

    public List<Log.Builder> getPartialLogsSoFar() {
        return partialLogs;
    }

    public void orchestrate(int currentLogCount, int finalLogCount) {
        Log.Builder partialLog = generatePartialLog(currentLogCount, finalLogCount);
        partialLogs.add(partialLog);
    }

    public void resume(List<Log.Builder> newPartialLogs) {
        partialLogs = newPartialLogs;
    }

    private Log.Builder generatePartialLog(int currentLogCount, int finalLogCount) {
        int difference = finalLogCount - currentLogCount;
        if (difference == complementPartialLogs.size()) {
            return complementPartialLogs.pop().withTime(SimpleTimeGenerator.generate());
        } else if (difference > complementPartialLogs.size()) {
            int choice = RandomChooser.chooseFrom(CHOICE_COUNT);
            Log.Builder partialLog = choices.get(choice).get();
            if (choice == 0 && partialLog.hasComplement()) {
                int newDifference = finalLogCount - (currentLogCount + 1);
                if (newDifference < complementPartialLogs.size()) {
                    undoChoice();
                    return generatePartialLog(currentLogCount, finalLogCount);
                }
            }
            return partialLog;
        } else {
            throw new AssertionError();
        }
    }

    private Log.Builder createFreshPartialLog() {
        LocalTime time = SimpleTimeGenerator.generate();
        Activity chosenActivity = RandomChooser.chooseFrom(activities);
        String subject = RandomChooser.chooseFrom(subjects);
        Log.Builder partialLog = Log.builder()
                .withTime(time)
                .withActivity(chosenActivity)
                .withSubject(subject);
        if (chosenActivity.hasComplement()) {
            Activity complementActivity = chosenActivity.getComplement();
            Log.Builder complementPartialLog = Log.builder()
                    .withActivity(complementActivity)
                    .withSubject(subject);
            complementPartialLogs.push(complementPartialLog);
        }
        return partialLog;
    }

    private Log.Builder chooseComplementPartialLog() {
        if (complementPartialLogs.isEmpty()) {
            return createFreshPartialLog();
        }
        Log.Builder complementPartialLog = complementPartialLogs.pop();
        return complementPartialLog.withTime(SimpleTimeGenerator.generate());
    }

    private void undoChoice() {
        complementPartialLogs.pop();
    }
}
