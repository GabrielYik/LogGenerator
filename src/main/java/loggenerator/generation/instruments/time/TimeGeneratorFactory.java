package loggenerator.generation.instruments.time;

public class TimeGeneratorFactory {
    public static TimeGenerator getTimeGenerator(String type) {
        switch (type) {
            case "simple":
                return new SimpleTimeGenerator();
            default:
                throw new AssertionError();
        }
    }
}
