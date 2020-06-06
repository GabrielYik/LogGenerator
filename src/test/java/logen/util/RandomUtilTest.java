package logen.util;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import util.RangeChecker;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilTest {
    @Test
    void chooseFrom_nullList_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> RandomUtil.chooseFrom(null));
    }

    @Test
    void chooseFrom_emptyList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseFrom(Collections.emptyList()));
    }

    @RepeatedTest(value = 5)
    void chooseFrom_validList_succeeds() {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer value = RandomUtil.chooseFrom(values);
        assertTrue(values.contains(value));
    }

    @Test
    void chooseBetweenInclusiveInteger_negativeBounds_throwsIllegalArgumentException() {
        int lowerBound = -1;
        int upperBound = 10;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound, upperBound));

        int lowerBound2 = 10;
        int upperBound2 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound2, upperBound2));

        int lowerBound3 = -1;
        int upperBound3 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound3, upperBound3));
    }

    @Test
    void chooseBetweenInclusiveInteger_lowerBoundGreaterThanUpperBound_throwsIllegalArgumentException() {
        int lowerBound = 10;
        int upperBound = 0;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound, upperBound));
    }

    @RepeatedTest(value = 5)
    void chooseBetweenInclusiveInteger_validBounds_succeeds() {
        int lowerBound = 0;
        int upperBound = 10;
        int value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertTrue(RangeChecker.inRange(value, lowerBound, upperBound));
    }

    @Test
    void chooseBetweenInclusiveInteger_equalBounds_succeeds() {
        int lowerBound = 10;
        int upperBound = 10;
        int value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertEquals(value, lowerBound);
    }

    @Test
    void chooseBetweenInclusiveLong_negativeBounds_throwsIllegalArgumentException() {
        long lowerBound = -1;
        long upperBound = 10;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound, upperBound));

        long lowerBound2 = 10;
        long upperBound2 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound2, upperBound2));

        long lowerBound3 = -1;
        long upperBound3 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound3, upperBound3));
    }

    @Test
    void chooseBetweenInclusiveLong_lowerBoundGreaterThanUpperBound_throwsIllegalArgumentException() {
        long lowerBound = 10;
        long upperBound = 0;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound, upperBound));
    }

    @RepeatedTest(value = 5)
    void chooseBetweenInclusiveLong_validBounds_succeeds() {
        long lowerBound = 0;
        long upperBound = 10;
        long value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertTrue(RangeChecker.inRange(value, lowerBound, upperBound));
    }

    @Test
    void chooseBetweenInclusiveLong_equalBounds_succeeds() {
        long lowerBound = 10;
        long upperBound = 10;
        long value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertEquals(value, lowerBound);
    }

    @Test
    void chooseBetweenInclusiveLocalTime_nullBounds_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> RandomUtil.chooseBetweenInclusive(null, null));
    }

    @Test
    void chooseBetweenInclusiveLocalTime_lowerBoundGreaterThanUpperBound_throwsIllegalArgumentException() {
        LocalTime lowerBound = LocalTime.of(10, 0);
        LocalTime upperBound = LocalTime.of(9, 0);
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.chooseBetweenInclusive(lowerBound, upperBound));
    }

    @RepeatedTest(value = 5)
    void chooseBetweenInclusiveLocalTime_validBounds_succeeds() {
        LocalTime lowerBound = LocalTime.of(9, 0);
        LocalTime upperBound = LocalTime.of(10, 0);
        LocalTime value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertTrue(RangeChecker.inRange(value, lowerBound, upperBound));
    }

    @Test
    void chooseBetweenInclusiveLocalTime_equalBounds_succeeds() {
        LocalTime lowerBound = LocalTime.of(9, 0);
        LocalTime upperBound = LocalTime.of(9, 0);
        LocalTime value = RandomUtil.chooseBetweenInclusive(lowerBound, upperBound);
        assertEquals(value, lowerBound);
    }

    @Test
    void distributeRandomlyInteger_negativeArguments_throwsIllegalArgumentException() {
        int sum = -1;
        int count = 1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum, count));

        int sum2 = 1;
        int count2 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum2, count2));

        int sum3 = -1;
        int count3 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum3, count3));
    }

    @Test
    void distributeRandomlyInteger_zeroValuedSum_returnListOfZeros() {
        int sum = 0;
        int count = 3;
        List<Integer> distribution = RandomUtil.distributeRandomly(sum, count);
        assertEquals(distribution, Collections.nCopies(count, 0));
    }

    @Test
    void distributeRandomlyInteger_zeroValuedCount_emptyList() {
        int sum = 3;
        int count = 0;
        List<Integer> distribution = RandomUtil.distributeRandomly(sum, count);
        assertEquals(distribution, Collections.emptyList());
    }

    @RepeatedTest(value = 5)
    void distributeRandomlyInteger_validArguments_succeeds() {
        int sum = 3;
        int count = 3;
        List<Integer> distribution = RandomUtil.distributeRandomly(sum, count);
        assertTrue(distribution.stream().allMatch(v -> 0 <= v && v <= sum));
        assertEquals(sum, distribution.stream().reduce(0, Integer::sum));

        int sum2 = 4;
        int count2 = 3;
        List<Integer> distribution2 = RandomUtil.distributeRandomly(sum2, count2);
        assertTrue(distribution2.stream().allMatch(v -> 0 <= v && v <= sum2));
        assertEquals(sum2, distribution2.stream().reduce(0, Integer::sum));

        int sum3 = 3;
        int count3 = 4;
        List<Integer> distribution3 = RandomUtil.distributeRandomly(sum3, count3);
        assertTrue(distribution3.stream().allMatch(v -> 0 <= v && v <= sum3));
        assertEquals(sum3, distribution3.stream().reduce(0, Integer::sum));
    }

    @Test
    void distributeRandomlyLong_negativeArguments_throwsIllegalArgumentException() {
        long sum = -1;
        int count = 1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum, count));

        long sum2 = 1;
        int count2 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum2, count2));

        long sum3 = -1;
        int count3 = -1;
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.distributeRandomly(sum3, count3));
    }

    @Test
    void distributeRandomlyLong_zeroValuedSum_returnListOfZeros() {
        long sum = 0;
        int count = 3;
        List<Long> distribution = RandomUtil.distributeRandomly(sum, count);
        assertEquals(distribution, Collections.nCopies(count, 0L));
    }

    @Test
    void distributeRandomlyLong_zeroValuedCount_emptyList() {
        long sum = 3;
        int count = 0;
        List<Long> distribution = RandomUtil.distributeRandomly(sum, count);
        assertEquals(distribution, Collections.emptyList());
    }

    @RepeatedTest(value = 5)
    void distributeRandomlyLong_validArguments_succeeds() {
        long sum = 3;
        int count = 3;
        List<Long> distribution = RandomUtil.distributeRandomly(sum, count);
        assertEquals(distribution, Collections.nCopies(count, sum / count));

        long sum2 = 4;
        int count2 = 3;
        List<Long> distribution2 = RandomUtil.distributeRandomly(sum2, count2);
        assertTrue(distribution2.stream().allMatch(v -> 0 <= v && v <= sum2));
        assertEquals(sum2, distribution2.stream().reduce(0L, Long::sum));

        long sum3 = 3;
        int count3 = 4;
        List<Long> distribution3 = RandomUtil.distributeRandomly(sum3, count3);
        assertTrue(distribution3.stream().allMatch(v -> 0 <= v && v <= sum3));
        assertEquals(sum3, distribution3.stream().reduce(0L, Long::sum));
    }
}
