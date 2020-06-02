package logen.generation;

import logen.generation.fixed.FixedLogGenerator;
import logen.generation.fixed.Fixture;
import logen.generation.filler.FillerLogGenerator;
import logen.log.Log;
import logen.scenario.Scenario;

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
