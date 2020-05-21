package logen.experimental.generation.fixed;

public enum SpacingType {
    ANY,
    CUSTOM;

    public static SpacingType map(String spacing) {
        return spacing.equals("any") ? ANY : CUSTOM;
    }
}
