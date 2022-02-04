package hzt.statistics;

import hzt.statistics.BigDecimalSummaryStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BigDecimalSummaryStatisticsTest {

    @Test
    void testThrowsIllegalArgumentExceptionWhenMinGreaterThanMax() {
        assertThrows(IllegalArgumentException.class,
                () -> new BigDecimalSummaryStatistics(0, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO));
    }

    @Test
    void testThrowsIllegalArgumentExceptionWhenCountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new BigDecimalSummaryStatistics(-1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE));
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow((ThrowingSupplier<BigDecimalSummaryStatistics>) BigDecimalSummaryStatistics::new);
    }
}
