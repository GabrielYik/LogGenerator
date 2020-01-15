package logen.generation.normal;

import logen.generation.TransitionContext;
import logen.log.Activity;
import logen.log.Log;
import logen.util.RandomChooser;
import logen.util.TemporalGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class NormalConductor {
    private List<Activity> activities;
    private List<String> subjects;

    private Stack<Log.Builder> complementPartialLogs;

    private List<Function<LocalTime, Log.Builder>> choices;
    private int choiceCount;

    public NormalConductor(List<Activity> activities, List<String> subjects) {
        this.activities = activities;
        this.subjects = subjects;

        complementPartialLogs = new Stack<>();

        choices = new ArrayList<>();
        choices.add(this::generateNewPartialLog);
        choices.add(this::chooseComplementPartialLog);
        choiceCount = choices.size();
    }

    public TransitionContext orchestrate(TransitionContext context, int currentLogCount, int finalLogCount) {
        List<Log.Builder> partialLogs = context.getPartialLogs();
        Log.Builder partialLog = generatePartialLog(currentLogCount, finalLogCount, context.getPreviousTime());
        partialLogs.add(partialLog);
        return new TransitionContext(partialLogs, partialLog.getTime());
    }

    private Log.Builder generatePartialLog(int currentLogCount, int finalLogCount, LocalTime previousTime) {
        int difference = finalLogCount - currentLogCount;
        if (difference == complementPartialLogs.size()) {
            return complementPartialLogs.pop().withTime(TemporalGenerator.generateTimeFrom(previousTime));
        } else if (difference > complementPartialLogs.size()) {
            int choice = RandomChooser.chooseFrom(choiceCount);
            Log.Builder partialLog = choices.get(choice).apply(previousTime);
            if (choice == 0 && partialLog.hasComplement()) {
                int newDifference = finalLogCount - (currentLogCount + 1);
                if (newDifference < complementPartialLogs.size()) {
                    undoChoice();
                    return generatePartialLog(currentLogCount, finalLogCount, previousTime);
                }
            }
            return partialLog;
        } else {
            throw new AssertionError();
        }
    }

    private Log.Builder generateNewPartialLog(LocalTime previousTime) {
        LocalTime time = TemporalGenerator.generateTimeFrom(previousTime);
        Activity chosenActivity = RandomChooser.chooseFrom(activities);
        if (!chosenActivity.hasSubject()) {
            String subject = RandomChooser.chooseFrom(subjects);
            chosenActivity.setSubject(subject);
        }
        Log.Builder partialLog = Log.builder()
            .withTime(time)
            .withActivity(chosenActivity);
        if (chosenActivity.hasComplement()) {
            Activity complementActivity = chosenActivity.getComplement();
            if (!complementActivity.hasSubject()) {
                String subject = RandomChooser.chooseFrom(subjects);
                complementActivity.setSubject(subject);
            }
            Log.Builder complementPartialLog = Log.builder()
                .withActivity(complementActivity);
            complementPartialLogs.push(complementPartialLog);
        }
        return partialLog;
    }

    private Log.Builder chooseComplementPartialLog(LocalTime previousTime) {
        if (complementPartialLogs.isEmpty()) {
            return generateNewPartialLog(previousTime);
        }
        Log.Builder complementPartialLog = complementPartialLogs.pop();
        return complementPartialLog.withTime(TemporalGenerator.generateTimeFrom(previousTime));
    }

    private void undoChoice() {
        complementPartialLogs.pop();
    }
}
