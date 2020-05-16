package logen.generation.suspicious;

import logen.generation.TransitionContext;
import logen.log.Activity;
import logen.generation.suspicious.troublemakers.TroubleMaker;
import logen.util.TemporalGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuspiciousConductor {
    private List<TroubleMaker> troubleMakers;
    private Iterator<TroubleMaker> troubleMakersIterator;

    public SuspiciousConductor(
        List<Activity> suspiciousActivities,
        List<Trouble> troubles,
        List<String> subjects,
        TemporalGenerator temporalGenerator) {
        troubleMakers = new ArrayList<>();
        if (troubles.isEmpty()) {
            TroubleMaker troubleMaker = TroubleMakerFactory.getTroubleMaker(null);
            troubleMakers.add(troubleMaker);
        } else {
            for (Trouble trouble : troubles) {
                TroubleMakerContext context = new TroubleMakerContext(trouble, suspiciousActivities, subjects, temporalGenerator);
                TroubleMaker troubleMaker = TroubleMakerFactory.getTroubleMaker(context);
                troubleMakers.add(troubleMaker);
            }
        }
        troubleMakersIterator = troubleMakers.iterator();
    }

    public TransitionContext orchestrate(TransitionContext context) {
        return troubleMakersIterator.next().makeTrouble(context);
    }

    public int getTroubleMakerCount() {
        return troubleMakers.size();
    }
}
