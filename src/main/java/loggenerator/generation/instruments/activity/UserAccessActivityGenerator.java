package loggenerator.generation.instruments.activity;

import loggenerator.generation.instruments.AbstractGenerator;
import loggenerator.model.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

public class UserAccessActivityGenerator extends AbstractGenerator implements ActivityGenerator {
    private static final int CHOICE_COUNT = 2;

    private Stack<Activity> complementActivities;
    private List<Supplier<Activity>> choices;
    private List<Activity> activities;

    public UserAccessActivityGenerator(List<Activity> activities) {
        super();
        complementActivities = new Stack<>();
        choices = new ArrayList<>();
        choices.add(this::chooseActivity);
        choices.add(this::chooseComplementActivity);
        this.activities = activities;
    }

    @Override
    public Activity generate(int currentLogCount, int finalLogCount) {
        int difference = finalLogCount - currentLogCount;
        if (difference == complementActivities.size()) {
            return complementActivities.pop();
        } else if (difference > complementActivities.size()) {
            int choice = choose();
            Activity chosenActivity = choices.get(choice).get();
            if (choice == 0) {
                if (chosenActivity.hasComplement()) {
                    int newDifference = finalLogCount - (currentLogCount + 1);
                    if (newDifference < complementActivities.size()) {
                        undoChoice();
                        return generate(currentLogCount, finalLogCount);
                    }
                }
            }
            return chosenActivity;
        } else {
            throw new AssertionError();
        }
    }

    private int choose() {
        return rng.nextInt(CHOICE_COUNT);
    }

    private boolean areComplementActivitiesEnough(int currentLogCount, int finalLogCount) {
        return finalLogCount - currentLogCount == complementActivities.size();
    }

    private Activity chooseActivity() {
        int randomIndex = rng.nextInt(activities.size());
        Activity chosenActivity = activities.get(randomIndex);
        if (chosenActivity.hasComplement()) {
            Activity complementActivity = chosenActivity.getComplement();
            complementActivities.push(complementActivity);
        }
        return chosenActivity;
    }

    private Activity chooseComplementActivity() {
        if (complementActivities.isEmpty()) {
            return chooseActivity();
        }
        return complementActivities.pop();
    }

    private void undoChoice() {
        complementActivities.pop();
    }
}
