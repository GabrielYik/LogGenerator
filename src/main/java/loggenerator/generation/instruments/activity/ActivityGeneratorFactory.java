package loggenerator.generation.instruments.activity;

import loggenerator.model.Activity;
import loggenerator.model.Log;

import java.util.List;

public class ActivityGeneratorFactory {
    public static ActivityGenerator getActivityGenerator(String type, List<Activity> activities) {
        switch (type) {
            case "user access":
                return new UserAccessActivityGenerator(activities);
            default:
                throw new AssertionError();
        }
    }
}
