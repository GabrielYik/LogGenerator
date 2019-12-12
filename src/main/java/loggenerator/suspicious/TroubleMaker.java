package loggenerator.suspicious;

import loggenerator.model.Log;

import java.util.List;

public interface TroubleMaker {
    List<Log.Builder> makeTrouble(List<Log.Builder> logs);
}
