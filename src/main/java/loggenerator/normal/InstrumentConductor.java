package loggenerator.normal;

import loggenerator.normal.instruments.RandomChooser;
import loggenerator.model.Log;
import loggenerator.model.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

public class InstrumentConductor {
    private static final int CHOICE_COUNT = 2;

    private int currentLogCount;
    private int finalLogCount;

    private List<Activity> activities;
    private List<String> subjects;
    private Stack<Log.Builder> complementPartialLogs;
    private List<Supplier<Log.Builder>> choices;

    public InstrumentConductor(
            int finalLogCount,
            List<Activity> activities,
            List<String> subjects) {
        currentLogCount = 0;
        this.finalLogCount = finalLogCount;
        this.activities = activities;
        this.subjects = subjects;

        complementPartialLogs = new Stack<>();
        choices = new ArrayList<>();
        choices.add(this::createFreshPartialLog);
        choices.add(this::chooseComplementPartialLog);
    }

    public List<Log.Builder> orchestrate() {
        List<Log.Builder> partialLogs = new ArrayList<>();
        while (currentLogCount != finalLogCount) {
            Log.Builder partialLog = generatePartialLog(currentLogCount, finalLogCount);
            partialLogs.add(partialLog);
            currentLogCount++;
        }
        return partialLogs;
    }

    public Log.Builder generatePartialLog(int currentLogCount, int finalLogCount) {
        int difference = finalLogCount - currentLogCount;
        if (difference == complementPartialLogs.size()) {
            return complementPartialLogs.pop();
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
        Activity chosenActivity = RandomChooser.chooseFrom(activities);
        String subject = RandomChooser.chooseFrom(subjects);
        Log.Builder partialLog = Log.builder().withActivity(chosenActivity).withSubject(subject);
        if (chosenActivity.hasComplement()) {
            Activity complementActivity = chosenActivity.getComplement();
            Log.Builder complementPartialLog = Log.builder().withActivity(complementActivity).withSubject(subject);
            complementPartialLogs.push(complementPartialLog);
        }
        return partialLog;
    }

    private Log.Builder chooseComplementPartialLog() {
        if (complementPartialLogs.isEmpty()) {
            return createFreshPartialLog();
        }
        return complementPartialLogs.pop();
    }

    private void undoChoice() {
        complementPartialLogs.pop();
    }
}
