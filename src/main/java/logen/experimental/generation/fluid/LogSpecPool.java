package logen.experimental.generation.fluid;

import logen.experimental.scenario.common.LogSpec;
import logen.experimental.util.RandomChooser;

import java.util.List;

public class LogSpecPool {
    private final List<LogSpec> logSpecs;

    public LogSpecPool(List<LogSpec> logSpecs) {
        this.logSpecs = logSpecs;
    }

    public LogSpec get() {
        LogSpec logSpec = RandomChooser.chooseFrom(logSpecs);
        //TODO use enum or constant
        if (logSpec.getFrequency().equals("one")) {
            logSpecs.remove(logSpec);
        }
        return logSpec;
    }
}
