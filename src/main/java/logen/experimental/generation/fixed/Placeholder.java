package logen.experimental.generation.fixed;

import logen.experimental.scenario.group.SpaceType;
import logen.experimental.scenario.time.TimePeriod;

/**
 * A representation of potential logs constrained by a time period and quantity.
 */
public class Placeholder {
    private final TimePeriod timePeriod;

    private final SpaceType spaceType;
    private final int spacingAmount;

    private Placeholder(TimePeriod timePeriod, SpaceType spaceType, int spacingAmount) {
        this.timePeriod = timePeriod;
        this.spaceType = spaceType;
        this.spacingAmount = spacingAmount;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public int getSpacingAmount() {
        return spacingAmount;
    }



    public static class Builder {
        private static final int DEFAULT_LOG_COUNT_FOR_SPACING_TYPE_ANY = 10;

        private TimePeriod timePeriod;

        private SpaceType spaceType;
        private int spaceAmount;

        public Builder() {

        }

        public Builder withTimePeriod(TimePeriod timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public Builder withSpaceType(SpaceType spaceType) {
            this.spaceType = spaceType;
            return this;
        }

        public Builder withSpaceAmount(int amount) {
            this.spaceAmount = amount;
            return this;
        }

        public Placeholder build() {
            return new Placeholder(timePeriod, spaceType, spaceAmount);
        }

        public SpaceType getSpaceType() {
            return spaceType;
        }

        public int getSpaceAmount() {
            return spaceAmount;
        }
    }
}
