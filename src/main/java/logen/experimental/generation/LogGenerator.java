package logen.experimental.generation;

import logen.experimental.generation.fixed.FixedLogGenerator;
import logen.experimental.generation.fixed.Fixture;
import logen.experimental.generation.filler.FillerLogGenerator;
import logen.experimental.log.Log;
import logen.experimental.scenario.Scenario;

import java.util.List;

/**
 * A generator of logs.
 */
public class LogGenerator {
    private final Scenario scenario;

    public LogGenerator(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Generates logs.
     * The logs are a mixture of fixed and filler logs.
     * @return A list of logs
     */
    public List<Log> generate() {
        Fixture fixture = new FixedLogGenerator(scenario).generate();
        return new FillerLogGenerator(scenario, fixture).generate();
    }
}
