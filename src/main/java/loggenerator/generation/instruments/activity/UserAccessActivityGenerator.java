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
        if (toPopFromStack(currentLogCount, finalLogCount)) {
            return complementActivities.pop();
        } else {
            int choice = choose();
            return choices.get(choice).get();
        }
    }

    private int choose() {
        return rng.nextInt(CHOICE_COUNT);
    }

    private boolean toPopFromStack(int currentLogCount, int finalLogCount) {
        return finalLogCount - currentLogCount == complementActivities.size();
    }

    private Activity chooseActivity() {
        int randomIndex = rng.nextInt(activities.size());
        Activity chosenActivity = activities.get(randomIndex);
        Activity complementActivity = chosenActivity.complement();
        complementActivities.push(complementActivity);
        return chosenActivity;
    }

    private Activity chooseComplementActivity() {
        if (complementActivities.isEmpty()) {
            return chooseActivity();
        }
        return complementActivities.pop();
    }
}
