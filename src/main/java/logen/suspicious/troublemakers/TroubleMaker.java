package logen.suspicious.troublemakers;

import logen.TransitionContext;

public interface TroubleMaker {
    TransitionContext makeTrouble(TransitionContext context);
}
