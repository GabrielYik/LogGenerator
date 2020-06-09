package logen.util.timegenerators;

import org.junit.jupiter.api.Test;
import util.RangeChecker;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedTimeGeneratorTest {

    @Test
    void between_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BoundedTimeGenerator.between(
           null, null, null, null
        ));
    }

    @Test
    void between_nonLinearArguments_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime toTime = LocalTime.of(10, 0);
        LocalTime wrapAroundTime = LocalTime.of(10, 0);
        LocalTime wrapToTime = LocalTime.of(9, 30);
        assertThrows(IllegalArgumentException.class, () -> BoundedTimeGenerator.between(
                fromTime, toTime, wrapAroundTime, wrapToTime
        ));
    }

    @Test
    void between_generate_validArguments_succeeds() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = BoundedTimeGenerator.between(
                fromTime, toTime, wrapAroundTime, wrapToTime
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .increasing()
                .from(fromTime)
                .to(toTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    @Test
    void between_generate_validArguments_wrapsAround() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(11, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = BoundedTimeGenerator.between(
                fromTime, toTime, wrapAroundTime, wrapToTime
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .increasing()
                .from(fromTime)
                .to(toTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }
}
