package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModelEvaluationServiceTest {

  private final ModelEvaluationService service =
      new ModelEvaluationService(
          new DemandFeatureEngineer(),
          new TemporalTrainTestSplitter(),
          new DemandRegressionTrainer(),
          new ForecastMetricCalculator());

  @Test
  void shouldEvaluateModelAgainstBaseline() {
    List<DailyDemand> series = createDemandSeries();

    ModelEvaluationResult result = service.evaluate(series);

    assertThat(result.trainingObservations()).isPositive();

    assertThat(result.testingObservations()).isPositive();

    assertThat(result.baselineMetrics().mae()).isFinite().isGreaterThanOrEqualTo(0.0);

    assertThat(result.baselineMetrics().rmse()).isFinite().isGreaterThanOrEqualTo(0.0);

    assertThat(result.modelMetrics().mae()).isFinite().isGreaterThanOrEqualTo(0.0);

    assertThat(result.modelMetrics().rmse()).isFinite().isGreaterThanOrEqualTo(0.0);
  }

  private List<DailyDemand> createDemandSeries() {
    List<DailyDemand> series = new ArrayList<>();

    int[] quantities = {
      12, 15, 11, 18, 14, 17, 20,
      13, 16, 12, 21, 18, 19, 23
    };

    for (int i = 0; i < quantities.length; i++) {
      series.add(new DailyDemand(1L, LocalDate.of(2026, 8, 1).plusDays(i), quantities[i]));
    }

    return series;
  }
}
