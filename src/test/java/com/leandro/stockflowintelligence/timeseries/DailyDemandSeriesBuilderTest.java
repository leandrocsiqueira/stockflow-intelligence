package com.leandro.stockflowintelligence.timeseries;

import static org.assertj.core.api.Assertions.assertThat;

import com.leandro.stockflowintelligence.domain.StockMovement;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DailyDemandSeriesBuilderTest {

  private final DailyDemandSeriesBuilder builder = new DailyDemandSeriesBuilder();

  @Test
  void shouldAggregateMovementsByDateForProduct() {
    List<StockMovement> movements =
        List.of(
            new StockMovement(1L, LocalDate.of(2026, 8, 1), 5),
            new StockMovement(1L, LocalDate.of(2026, 8, 1), 7),
            new StockMovement(1L, LocalDate.of(2026, 8, 2), 10));

    List<DailyDemand> series = builder.build(movements, 1L);

    assertThat(series)
        .containsExactly(
            new DailyDemand(1L, LocalDate.of(2026, 8, 1), 12),
            new DailyDemand(1L, LocalDate.of(2026, 8, 2), 10));
  }

  @Test
  void shouldIgnoreMovementsFromOtherProducts() {
    List<StockMovement> movements =
        List.of(
            new StockMovement(1L, LocalDate.of(2026, 8, 1), 10),
            new StockMovement(2L, LocalDate.of(2026, 8, 1), 50));

    List<DailyDemand> series = builder.build(movements, 1L);

    assertThat(series).containsExactly(new DailyDemand(1L, LocalDate.of(2026, 8, 1), 10));
  }
}
