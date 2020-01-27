package logen.generation.suspicious.troublemakers;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import logen.generation.TransitionContext;
import logen.generation.suspicious.Trouble;
import logen.log.Log;
import logen.util.RandomChooser;

public class RemoverTroubleMaker implements TroubleMaker {
    private Trouble trouble;

    public RemoverTroubleMaker(Trouble trouble) {
        this.trouble = trouble;
    }

    @Override
    public TransitionContext makeTrouble(TransitionContext context) {
        List<Log.Builder> partialLogs = context.getPartialLogs();
        int attackPoint = RandomChooser.chooseFrom(partialLogs.size());
        ListIterator<Log.Builder> partialLogsIterator = partialLogs.listIterator();
        int currentIndex = 0;
        int currentRemovalCount = 0;
        int finalRemovalCount = trouble.getCount();
        while (true) {
            if (currentIndex == attackPoint) {
                while (currentRemovalCount != finalRemovalCount) {
                    partialLogsIterator.next();
                    partialLogsIterator.remove();
                    currentRemovalCount++;
                }
                break;
            } else {
                partialLogsIterator.next();
                currentIndex++;
            }
        }
        return new TransitionContext(partialLogs);
    }
}
