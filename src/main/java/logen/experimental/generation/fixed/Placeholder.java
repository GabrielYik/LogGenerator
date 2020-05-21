package logen.experimental.generation.fixed;

public class Placeholder {
    private static final int DEFAULT_LOG_COUNT_FOR_SPACING_TYPE_ANY = 10;
    private final SpacingType spacingType;
    private final int spacingAmount;

    public Placeholder(String spacing) {
        this.spacingType = SpacingType.map(spacing);
        switch(this.spacingType) {
            case ANY:
                spacingAmount = -1;
                break;
            case CUSTOM:
                spacingAmount = Integer.parseInt(spacing);
                break;
            default:
                throw new AssertionError();
        }
    }

    public SpacingType getSpacingType() {
        return spacingType;
    }

    public int getSpacingAmount() {
        if (spacingType.equals(SpacingType.ANY)) {
            return DEFAULT_LOG_COUNT_FOR_SPACING_TYPE_ANY;
        }
        return spacingAmount;
    }
}
