package logen.generation.suspicious;

import logen.generation.suspicious.troublemakers.EmptyTroubleMaker;
import logen.generation.suspicious.troublemakers.MultiplierTroubleMaker;
import logen.generation.suspicious.troublemakers.OddHoursTroubleMaker;
import logen.generation.suspicious.troublemakers.RemoverTroubleMaker;
import logen.generation.suspicious.troublemakers.TroubleMaker;

public class TroubleMakerFactory {
    public static TroubleMaker getTroubleMaker(TroubleMakerContext context) {
        if (context == null) {
            return new EmptyTroubleMaker();
        }
        switch (context.getTrouble().getType()) {
        case "high volume":
            return new MultiplierTroubleMaker(
                context.getTrouble(),
                context.getSuspiciousActivities().get(0),
                context.getSubjects(),
                context.getTemporalGenerator());
        case "odd hours":
            return new OddHoursTroubleMaker(
                context.getTrouble(),
                context.getSuspiciousActivities(),
                context.getSubjects());
        case "remover":
            return new RemoverTroubleMaker(context.getTrouble());
        default:
            return new EmptyTroubleMaker();
        }
    }
}
