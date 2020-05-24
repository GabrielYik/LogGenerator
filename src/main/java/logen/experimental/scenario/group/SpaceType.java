package logen.experimental.scenario.group;

public enum SpaceType {
    ANY,
    CUSTOM;

    public static SpaceType map(String spacing) {
        return spacing.equals("any") ? ANY : CUSTOM;
    }
}
