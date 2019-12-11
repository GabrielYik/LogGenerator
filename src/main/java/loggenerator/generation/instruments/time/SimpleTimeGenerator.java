package loggenerator.generation.instruments.time;

import java.time.LocalDateTime;

public class SimpleTimeGenerator implements TimeGenerator {
    @Override
    public LocalDateTime generate() {
        return LocalDateTime.now();
    }
}
