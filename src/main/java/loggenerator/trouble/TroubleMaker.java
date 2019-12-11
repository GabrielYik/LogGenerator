package loggenerator.trouble;

import loggenerator.model.Log;

import java.util.List;

public interface TroubleMaker {
    List<Log> makeTrouble(List<Log> logs);
}
