package loggenerator.suspicious;

public class TroubleMakerFactory {
    public static TroubleMaker getTroubleMaker(TroubleMakingContext context) {
        switch (context.getType()) {
            case "high volume":
                return new HighVolumeTroubleMaker(context.getSuspiciousLog());
            default:
                throw new AssertionError();
        }
    }
}
