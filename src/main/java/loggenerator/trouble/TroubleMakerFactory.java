package loggenerator.trouble;

public class TroubleMakerFactory {
    public static TroubleMaker getTroubleMaker(String type) {
        switch (type) {
            case "high volume":
                return new HighVolumeTroubleMaker();
            default:
                throw new AssertionError();
        }
    }
}
