package logen.experimental.generation.fixed;

import logen.experimental.scenario.TimePeriod;

/**
 * A representation of potential logs constrained by a time period and quantity.
 */
public class Placeholder {
    private final TimePeriod timePeriod;

    private final SpacingType spacingType;
    private final int spacingAmount;

    private Placeholder(TimePeriod timePeriod, SpacingType spacingType, int spacingAmount) {
        this.timePeriod = timePeriod;
        this.spacingType = spacingType;
        this.spacingAmount = spacingAmount;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public SpacingType getSpacingType() {
        return spacingType;
    }

    public int getSpacingAmount() {
        return spacingAmount;
    }

    public static class Builder {
        private static final int DEFAULT_LOG_COUNT_FOR_SPACING_TYPE_ANY = 10;

        private TimePeriod timePeriod;

        private SpacingType spacingType;
        private int spacingAmount;

        public Builder() {

        }

        public Builder withTimePeriod(TimePeriod timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public Builder withSpacing(String spacing) {
            spacingType = SpacingType.map(spacing);
            switch(spacingType) {
                case ANY:
                    spacingAmount = DEFAULT_LOG_COUNT_FOR_SPACING_TYPE_ANY;
                    break;
                case CUSTOM:
                    spacingAmount = Integer.parseInt(spacing);
                    break;
                default:
                    throw new AssertionError();
            }
            return this;
        }

        public Placeholder build() {
            return new Placeholder(timePeriod, spacingType, spacingAmount);
        }

        public int getSpacingAmount() {
            return spacingAmount;
        }
    }
}
