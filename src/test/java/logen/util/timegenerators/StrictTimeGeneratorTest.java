package logen.util.timegenerators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.RangeChecker;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrictTimeGeneratorTest {

    @Test
    void wrap_nullLocalTimeInstances_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StrictTimeGenerator.wrap(
                null, null, null, null, 1
        ));
    }

    @Test
    void wrap_nonLinearLocalTimeInstances_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(11, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        int generationCount = 1;
        assertThrows(IllegalArgumentException.class, () -> StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, generationCount
        ));
    }

    @Test
    void wrap_negativeGenerationCount_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        assertThrows(IllegalArgumentException.class, () -> StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, -1
        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 100})
    void wrap_generate_validArguments_succeeds(int generationCount) {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, generationCount
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        assertTrue(RangeChecker
                .forLocalTime(values)
                .increasing()
                .from(fromTime)
                .to(toTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 100})
    void wrap_generate_validArguments_firstValueWrapsAround(int generationCount) {
        LocalTime fromTime = LocalTime.of(17, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        TimeGenerator timeGenerator = StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, generationCount
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        assertTrue(RangeChecker
                .forLocalTime(values)
                .increasing()
                .from(fromTime)
                .to(toTime)
                .wrapsAround(wrapAroundTime)
                .wrapsTo(wrapToTime)
                .check()
        );
    }

    /**
     * It is difficult to test {@link StrictTimeGenerator#skip(int)} since
     * it is inherently random.
     * Hence, in this test method, we test only that the number of time
     * values skipped is correct.
     */
    @Test
    void wrap_skip_validArguments_skipsCorrectly() {
        LocalTime fromTime = LocalTime.of(17, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        LocalTime wrapAroundTime = LocalTime.of(17, 0);
        LocalTime wrapToTime = LocalTime.of(9, 0);
        int generationCount = 10;
        TimeGenerator timeGenerator = StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, generationCount
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());

        StrictTimeGenerator timeGeneratorForSkipping = StrictTimeGenerator.wrap(
                fromTime, toTime, wrapAroundTime, wrapToTime, generationCount
        );
        List<LocalTime> skippedValues = new ArrayList<>();
        int skipCount = 0;
        for (int i = 0; i < values.size(); i++) {
            if (i % 2 == 0) {
                skippedValues.add(timeGeneratorForSkipping.generate());
            } else {
                timeGeneratorForSkipping.skip(1);
                skipCount++;
            }
        }
        skippedValues = skippedValues.stream().filter(Objects::nonNull).collect(Collectors.toList());

        assertEquals(values.size() - skippedValues.size(), skipCount);
    }

    @Test
    void linear_nullLocalTimeInstances_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StrictTimeGenerator.linear(
                null, null, 1
        ));
    }

    @Test
    void linear_nonLinearLocalTimeInstances_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(15, 0);
        LocalTime toTime = LocalTime.of(12, 0);
        int generationCount = 1;
        assertThrows(IllegalArgumentException.class, () -> StrictTimeGenerator.linear(
                fromTime, toTime, generationCount
        ));
    }

    @Test
    void linear_negativeGenerationCount_throwsIllegalArgumentException() {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        assertThrows(IllegalArgumentException.class, () -> StrictTimeGenerator.linear(
                fromTime, toTime, -1
        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 5, 7, 100})
    void linear_generate_validArguments_succeeds(int generationCount) {
        LocalTime fromTime = LocalTime.of(12, 0);
        LocalTime toTime = LocalTime.of(15, 0);
        TimeGenerator timeGenerator = StrictTimeGenerator.linear(
                fromTime, toTime, generationCount
        );
        List<LocalTime> values = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            values.add(timeGenerator.generate());
        }
        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        assertTrue(RangeChecker
                .forLocalTime(values)
                .increasing()
                .from(fromTime)
                .to(toTime)
                .check()
        );
    }
}
