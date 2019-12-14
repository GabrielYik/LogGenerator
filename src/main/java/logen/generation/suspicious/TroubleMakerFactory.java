package logen.generation.suspicious;

import logen.generation.suspicious.troublemakers.MultiplierTroubleMaker;
import logen.generation.suspicious.troublemakers.OddHoursTroubleMaker;
import logen.generation.suspicious.troublemakers.TroubleMaker;

public class TroubleMakerFactory {
    public static TroubleMaker getTroubleMaker(TroubleMakingContext context) {
        switch (context.getTrouble().getType()) {
        case "high volume":
            return new MultiplierTroubleMaker(
                context.getTrouble(),
                context.getSuspiciousActivities().get(0),
                context.getSubjects().get(0));
        case "odd hours":
            return new OddHoursTroubleMaker(
                context.getTrouble(),
                context.getSuspiciousActivities(),
                context.getSubjects());
        default:
            throw new AssertionError();
        }
    }
}
