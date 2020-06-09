package util;

import java.time.LocalTime;
import java.util.List;

public class RangeChecker {
    private RangeChecker() {

    }

    public static boolean inRange(int value, int lowerBound, int upperBound) {
        return lowerBound <= value && value <= upperBound;
    }

    public static boolean inRange(long value, long lowerBound, long upperBound) {
        return lowerBound <= value && value <= upperBound;
    }

    public static boolean inRange(LocalTime value, LocalTime lowerBound, LocalTime upperBound) {
        return (lowerBound.isBefore(value) && value.isBefore(upperBound))
                || value.equals(lowerBound)
                || value.equals(upperBound);
    }

    public static LocalTimeRangeChecker forLocalTime(List<LocalTime> values) {
        return new LocalTimeRangeChecker(values);
    }

    public static class LocalTimeRangeChecker {
        private final List<LocalTime> values;
        private LocalTime fromTime;
        private LocalTime toTime;
        private LocalTime wrapAroundTime;
        private LocalTime wrapToTime;

        private long lowerBound;
        private long upperBound;

        private Tendency tendency;

        private LocalTimeRangeChecker(List<LocalTime> values) {
            this.values = values;
        }

        public LocalTimeRangeChecker withInterval(long lowerBound, long upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            return this;
        }

        public LocalTimeRangeChecker increasing() {
            tendency = Tendency.INCREASING;
            return this;
        }

        public LocalTimeRangeChecker decreasing() {
            tendency = Tendency.DECREASING;
            return this;
        }

        public LocalTimeRangeChecker from(LocalTime fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        public LocalTimeRangeChecker to(LocalTime toTime) {
            this.toTime = toTime;
            return this;
        }

        public LocalTimeRangeChecker wrapsAround(LocalTime wrapAroundTime) {
            this.wrapAroundTime = wrapAroundTime;
            return this;
        }

        public LocalTimeRangeChecker wrapsTo(LocalTime wrapToTime) {
            this.wrapToTime = wrapToTime;
            return this;
        }

        public boolean check() {
            if (fromTime != null) {
                values.add(0, fromTime);
            }
            if (toTime != null) {
                values.add(toTime);
            }

            if (wrapToTime == null || wrapAroundTime == null) {
                if (tendency == Tendency.INCREASING) {
                    wrapAroundTime = toTime;
                    wrapToTime = fromTime;
                } else {
                    throw new AssertionError();
                }
            }

            boolean inRange = checkRange();
            if (!inRange) {
                return false;
            }

            boolean inOrder = checkOrder();
            if (!inOrder) {
                return false;
            }

            if (lowerBound != 0L || upperBound != 0L) {
                return checkInterval();
            }
            return true;
        }

        private boolean checkRange() {
            switch (tendency) {
                case INCREASING:
                    return values.stream().allMatch(v -> inRange(v, wrapToTime, wrapAroundTime));
                case DECREASING:
                    return values.stream().allMatch(v -> inRange(v, wrapAroundTime, wrapToTime));
                default:
                    throw new AssertionError();
            }
        }

        private boolean checkOrder() {
            switch (tendency) {
                case INCREASING:
                    for (int i = 0; i < values.size() - 1; i++) {
                        LocalTime value = values.get(i);
                        LocalTime nextValue = values.get(i + 1);
                        if (value.isAfter(nextValue) && !nextValue.isAfter(wrapToTime)) {
                            return false;
                        }
                    }
                    break;
                case DECREASING:
                    for (int i = values.size() - 1; i > 0; i--) {
                        LocalTime value = values.get(i);
                        LocalTime nextValue = values.get(i - 1);
                        if (value.isBefore(nextValue) && !nextValue.isBefore(wrapToTime)) {
                            return false;
                        }
                    }
                    break;
                default:
                    throw new AssertionError();
            }
            return true;
        }

        private boolean checkInterval() {
            for (int i = 0; i < values.size() - 1; i++) {
                LocalTime value = values.get(i);
                LocalTime nextValue = values.get(i + 1);
                if (value.equals(nextValue)) {
                    return false;
                }

                switch (tendency) {
                    case INCREASING:
                        if (value.isBefore(nextValue)) {
                            long secondsDifference = nextValue.toSecondOfDay() - value.toSecondOfDay();
                            if (!inRange(secondsDifference, lowerBound, upperBound)) {
                                return false;
                            }
                        }
                        if (value.isAfter(nextValue)) {
                            long secondsDifferenceBeforeWrap = wrapAroundTime.toSecondOfDay() - value.toSecondOfDay();
                            if (secondsDifferenceBeforeWrap > upperBound) {
                                return false;
                            }
                            long secondsDifferenceAfterWrap = nextValue.toSecondOfDay() - wrapToTime.toSecondOfDay();
                            if (!inRange(secondsDifferenceAfterWrap, lowerBound, upperBound)) {
                                return false;
                            }
                        }
                        break;
                    case DECREASING:
                        if (value.isBefore(nextValue)) {
                            long secondsDifferenceBeforeWrap = value.toSecondOfDay() - wrapAroundTime.toSecondOfDay();
                            if (secondsDifferenceBeforeWrap > upperBound) {
                                return false;
                            }
                            long secondsDifferenceAfterWrap = wrapToTime.toSecondOfDay() - nextValue.toSecondOfDay();
                            if (!inRange(secondsDifferenceAfterWrap, lowerBound, upperBound)) {
                                return false;
                            }
                        }
                        if (value.isAfter(nextValue)) {
                            long secondsDifference = value.toSecondOfDay() - nextValue.toSecondOfDay();
                            if (!inRange(secondsDifference, lowerBound, upperBound)) {
                                return false;
                            }
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            return true;
        }

        private enum Tendency {
            INCREASING,
            DECREASING
        }
    }
}
