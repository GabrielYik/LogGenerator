package logen.util.timegenerators;

import org.junit.jupiter.api.Test;
import util.RangeChecker;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnboundedTimeGeneratorTest {
    @Test
    void forward_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UnboundedTimeGenerator.forward(
                null, null, null
        ));
    }

    @Test
    void forward_nonLinearArguments_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime wrapAroundTime = LocalTime.of(10, 0);
        LocalTime wrapToTime = LocalTime.of(9, 30);
        assertThrows(IllegalArgumentException.class, () -> UnboundedTimeGenerator.forward(
                fromTime, wrapAroundTime, wrapToTime
        ));
    }

    @Test
    void forward_generate_validArguments_succeeds() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = UnboundedTimeGenerator.forward(fromTime, wrapAroundTime, wrapToTime);
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .increasing()
                .from(fromTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    @Test
    void forward_generate_validArguments_firstTimeValueWrapsAround() {
        LocalTime fromTime = LocalTime.of(17, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = UnboundedTimeGenerator.forward(fromTime, wrapAroundTime, wrapToTime);
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .increasing()
                .from(fromTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    @Test
    void backward_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UnboundedTimeGenerator.backward(
                null, null, null
        ));
    }

    @Test
    void backward_nonLinearArguments_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime wrapAroundTime = LocalTime.of(9, 30);
        LocalTime wrapToTime = LocalTime.of(10, 0);
        assertThrows(IllegalArgumentException.class, () -> UnboundedTimeGenerator.backward(
                fromTime, wrapAroundTime, wrapToTime
        ));
    }

    @Test
    void backward_generate_validArguments_succeeds() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime wrapAroundTime = LocalTime.of(9, 0);
        LocalTime wrapToTime = LocalTime.of(17, 0);
        TimeGenerator timeGenerator = UnboundedTimeGenerator.backward(fromTime, wrapAroundTime, wrapToTime);
        List<LocalTime> values = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .decreasing()
                .from(fromTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    @Test
    void backward_generate_validArguments_firstTimeValueWrapsAround() {
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime wrapAroundTime = LocalTime.of(9, 0);
        LocalTime wrapToTime = LocalTime.of(17, 0);
        TimeGenerator timeGenerator = UnboundedTimeGenerator.backward(fromTime, wrapAroundTime, wrapToTime);
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        assertTrue(RangeChecker
                .forLocalTime(values)
                .withInterval(AbstractTimeGenerator.SMALLEST_INTERVAL_DIFFERENCE, AbstractTimeGenerator.LARGEST_INTERVAL_DIFFERENCE)
                .decreasing()
                .from(fromTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }
}
