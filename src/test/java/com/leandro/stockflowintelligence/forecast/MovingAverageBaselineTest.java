package com.leandro.stockflowintelligence.forecast;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class MovingAverageBaselineTest {

  private final MovingAverageBaseline baseline = new MovingAverageBaseline();

  @Test
  void shouldForecastUsingLastSevenDaysAverage() {
    List<DailyDemand> series =
        List.of(
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 10),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 12),
            new DailyDemand(1L, LocalDate.of(2026, 8, 3), 8),
            new DailyDemand(1L, LocalDate.of(2026, 8, 4), 14),
            new DailyDemand(1L, LocalDate.of(2026, 8, 5), 6),
            new DailyDemand(1L, LocalDate.of(2026, 8, 6), 10),
            new DailyDemand(1L, LocalDate.of(2026, 8, 7), 10));

    double forecast = baseline.forecast(series);

    assertThat(forecast).isEqualTo(10.0);
  }

  @Test
  void shouldUseOnlyMostRecentSevenDays() {
    List<DailyDemand> series =
        List.of(
            new DailyDemand(1L, LocalDate.of(2026, 7, 31), 100),
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 3), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 4), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 5), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 6), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 7), 7));

    double forecast = baseline.forecast(series);

    assertThat(forecast).isEqualTo(7.0);
  }

  @Test
  void shouldHandleUnorderedSeries() {
    List<DailyDemand> series =
        List.of(
            new DailyDemand(1L, LocalDate.of(2026, 8, 7), 14),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 5), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 6), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 3), 7),
            new DailyDemand(1L, LocalDate.of(2026, 8, 4), 7));

    double forecast = baseline.forecast(series);

    assertThat(forecast).isEqualTo(8.0);
  }

  @Test
  void shouldRejectSeriesWithLessThanSevenDays() {
    List<DailyDemand> series =
        List.of(
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 10),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 12));

    assertThatThrownBy(() -> baseline.forecast(series))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("At least 7 daily demand observations are required");
  }
}
