package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DemandFeatureEngineerTest {

  private final DemandFeatureEngineer engineer = new DemandFeatureEngineer();

  @Test
  void shouldGenerateFeaturesUsingOnlyPreviousObservations() {
    List<DailyDemand> series =
        List.of(
            demand(1, 10),
            demand(2, 12),
            demand(3, 14),
            demand(4, 16),
            demand(5, 18),
            demand(6, 20),
            demand(7, 22),
            demand(8, 24));

    List<DemandFeature> features = engineer.generate(series);

    assertThat(features).hasSize(1);

    DemandFeature feature = features.getFirst();

    assertThat(feature.date()).isEqualTo(LocalDate.of(2026, 8, 8));

    assertThat(feature.lag1()).isEqualTo(22.0);
    assertThat(feature.lag7()).isEqualTo(10.0);
    assertThat(feature.movingAverage7()).isEqualTo(16.0);
    assertThat(feature.target()).isEqualTo(24.0);

    assertThat(feature.standardDeviation7()).isCloseTo(4.0, within(0.000001));
  }

  @Test
  void shouldOrderSeriesBeforeGeneratingFeatures() {
    List<DailyDemand> series =
        List.of(
            demand(8, 24),
            demand(2, 12),
            demand(6, 20),
            demand(1, 10),
            demand(5, 18),
            demand(7, 22),
            demand(3, 14),
            demand(4, 16));

    List<DemandFeature> features = engineer.generate(series);

    assertThat(features).hasSize(1);
    assertThat(features.getFirst().date()).isEqualTo(LocalDate.of(2026, 8, 8));
    assertThat(features.getFirst().lag1()).isEqualTo(22.0);
  }

  @Test
  void shouldRejectInsufficientHistory() {
    List<DailyDemand> series = List.of(demand(1, 10), demand(2, 12), demand(3, 14));

    assertThatThrownBy(() -> engineer.generate(series))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("At least 8 daily demand observations are required");
  }

  private DailyDemand demand(int day, int quantity) {
    return new DailyDemand(1L, LocalDate.of(2026, 8, day), quantity);
  }
}
