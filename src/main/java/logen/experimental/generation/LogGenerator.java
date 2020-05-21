package logen.experimental.generation;

import logen.experimental.generation.fixed.FixedLogGenerator;
import logen.experimental.generation.fixed.Fixture;
import logen.experimental.generation.fluid.FluidLogGenerator;
import logen.experimental.log.Log;
import logen.experimental.scenario.Scenario;

import java.util.List;

public class LogGenerator {
    private final Scenario scenario;

    public LogGenerator(Scenario scenario) {
        this.scenario = scenario;
    }

    public List<Log> generate() {
        Fixture fixture = new FixedLogGenerator(scenario).generate();
        return new FluidLogGenerator(scenario, fixture).generate();
    }

}
