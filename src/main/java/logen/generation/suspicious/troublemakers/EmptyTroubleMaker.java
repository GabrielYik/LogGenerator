package logen.generation.suspicious.troublemakers;

import logen.generation.TransitionContext;

public class EmptyTroubleMaker implements TroubleMaker {
    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        return context;
    }
}
