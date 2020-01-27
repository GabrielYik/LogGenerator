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
import java.util.function.Supplier;

public class NormalConductor {
    private List<Activity> activities;
    private List<String> subjects;
    private TemporalGenerator temporalGenerator;

    private Stack<Log.Builder> complementPartialLogs;

    private List<Supplier<Log.Builder>> choices;
    private int choiceCount;

    public NormalConductor(List<Activity> activities, List<String> subjects, TemporalGenerator temporalGenerator) {
        this.activities = activities;
        this.subjects = subjects;
        this.temporalGenerator = temporalGenerator;

        complementPartialLogs = new Stack<>();

        choices = new ArrayList<>();
        choices.add(this::generateNewPartialLog);
        choices.add(this::chooseComplementPartialLog);
        choiceCount = choices.size();
    }

    public TransitionContext orchestrate(TransitionContext context, int currentLogCount, int finalLogCount) {
        List<Log.Builder> partialLogs = context.getPartialLogs();
        Log.Builder partialLog = generatePartialLog(currentLogCount, finalLogCount);
        partialLogs.add(partialLog);
        return new TransitionContext(partialLogs);
    }

    private Log.Builder generatePartialLog(int currentLogCount, int finalLogCount) {
        int difference = finalLogCount - currentLogCount;
        if (difference == complementPartialLogs.size()) {
            return complementPartialLogs.pop().withTime(temporalGenerator.generateTime());
        } else if (difference > complementPartialLogs.size()) {
            int choice = RandomChooser.chooseFrom(choiceCount);
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

    private void undoChoice() {
        complementPartialLogs.pop();
    }

    private Log.Builder generateNewPartialLog() {
        LocalTime time = temporalGenerator.generateTime();
        Activity chosenActivity = RandomChooser.chooseFrom(activities);
        if (!chosenActivity.hasSubject()) {
            String subject = RandomChooser.chooseFrom(subjects);
            chosenActivity.setSubject(subject);
        }
        Log.Builder partialLog = Log.builder()
            .withTime(time)
            .withActivity(chosenActivity);
        if (chosenActivity.hasComplement()) {
            Activity complement = chosenActivity.getComplement();
            storeComplement(complement);
        }
        return partialLog;
    }

    private Log.Builder chooseComplementPartialLog() {
        if (complementPartialLogs.isEmpty()) {
            return generateNewPartialLog();
        }
        Log.Builder complementPartialLog = complementPartialLogs.pop();
        if (complementPartialLog.hasComplement()) {
            Activity complement = complementPartialLog.getComplement();
            storeComplement(complement);
        }
        return complementPartialLog.withTime(temporalGenerator.generateTime());
    }

    private void storeComplement(Activity complement) {
        if (!complement.hasSubject()) {
            String subject = RandomChooser.chooseFrom(subjects);
            complement.setSubject(subject);
        }
        Log.Builder complementPartialLog = Log.builder()
            .withActivity(complement);
        complementPartialLogs.push(complementPartialLog);
    }
}
