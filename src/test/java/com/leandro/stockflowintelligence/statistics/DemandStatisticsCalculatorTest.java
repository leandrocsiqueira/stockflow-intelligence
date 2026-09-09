package com.leandro.stockflowintelligence.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DemandStatisticsCalculatorTest {

  private final DemandStatisticsCalculator calculator = new DemandStatisticsCalculator();

  @Test
  void shouldCalculateDescriptiveStatistics() {
    List<DailyDemand> series =
        List.of(
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 10),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 12),
            new DailyDemand(1L, LocalDate.of(2026, 8, 3), 8),
            new DailyDemand(1L, LocalDate.of(2026, 8, 4), 14),
            new DailyDemand(1L, LocalDate.of(2026, 8, 5), 6));

    DemandStatistics statistics = calculator.calculate(series);

    assertThat(statistics.mean()).isEqualTo(10.0);
    assertThat(statistics.median()).isEqualTo(10.0);
    assertThat(statistics.variance()).isEqualTo(10.0);
    assertThat(statistics.standardDeviation()).isCloseTo(Math.sqrt(10.0), within(0.000001));
  }

  @Test
  void shouldRejectEmptyDemandSeries() {
    assertThatThrownBy(() -> calculator.calculate(List.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Series must not be null or empty");
  }
}
