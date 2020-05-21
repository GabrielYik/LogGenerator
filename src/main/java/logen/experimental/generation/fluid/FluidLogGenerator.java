package logen.experimental.generation.fluid;

import logen.experimental.generation.fixed.Fixture;
import logen.experimental.log.Log;
import logen.experimental.scenario.Scenario;

import java.util.List;

public class FluidLogGenerator {
    private final Scenario scenario;
    private final Fixture fixture;

    public FluidLogGenerator(Scenario scenario, Fixture fixture) {
        this.scenario = scenario;
        this.fixture = fixture;
    }

    public List<Log> generate() {
        return null;
    }
}
