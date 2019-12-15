package logen.generation;

import logen.generation.suspicious.Trouble;
import logen.generation.suspicious.TroubleMakerFactory;
import logen.generation.suspicious.TroubleMakingContext;
import logen.log.Activity;
import logen.generation.suspicious.troublemakers.TroubleMaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TroublePipeline {
    private List<TroubleMaker> troubleMakers;
    private Iterator<TroubleMaker> troubleMakersIterator;

    public TroublePipeline(List<Activity> suspiciousActivities, List<Trouble> troubles, List<String> subjects) {
        troubleMakers = new ArrayList<>();
        for (Trouble trouble : troubles) {
            TroubleMakingContext context = new TroubleMakingContext(trouble, suspiciousActivities, subjects);
            TroubleMaker troubleMaker = TroubleMakerFactory.getTroubleMaker(context);
            troubleMakers.add(troubleMaker);
        }
        troubleMakersIterator = troubleMakers.iterator();
    }

    public TransitionContext runNext(TransitionContext context) {
        return troubleMakersIterator.next().makeTrouble(context);
    }

    public int getTroubleMakerCount() {
        return troubleMakers.size();
    }
}
