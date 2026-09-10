package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.leandro.stockflowintelligence.data.StockMovementCsvReader;
import com.leandro.stockflowintelligence.timeseries.DailyDemandSeriesBuilder;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class DemandModelEvaluationServiceTest {

  private final DemandModelEvaluationService service =
      new DemandModelEvaluationService(
          new StockMovementCsvReader(),
          new DailyDemandSeriesBuilder(),
          new ModelEvaluationService(
              new DemandFeatureEngineer(),
              new TemporalTrainTestSplitter(),
              new DemandRegressionTrainer(),
              new ForecastMetricCalculator()));

  @Test
  void shouldEvaluateModelUsingFixedDataset() {
    ModelEvaluationResult result = service.evaluate("data/stock-movements.csv", 1L);

    assertThat(result.trainingObservations()).isEqualTo(5);

    assertThat(result.testingObservations()).isEqualTo(2);

    assertThat(result.baselineMetrics().mae()).isCloseTo(4.1428571429, within(0.000001));

    assertThat(result.baselineMetrics().rmse()).isCloseTo(4.5400710234, within(0.000001));

    assertThat(result.modelMetrics().mae()).isFinite().isGreaterThanOrEqualTo(0.0);

    assertThat(result.modelMetrics().rmse()).isFinite().isGreaterThanOrEqualTo(0.0);

    printEvaluation(result);
  }

  private void printEvaluation(ModelEvaluationResult result) {

    System.out.println();
    System.out.println("=== MODEL EVALUATION ===");

    System.out.printf(Locale.ROOT, "%-12s %10s %10s%n", "Model", "MAE", "RMSE");

    System.out.printf(
        Locale.ROOT,
        "%-12s %10.4f %10.4f%n",
        "Baseline",
        result.baselineMetrics().mae(),
        result.baselineMetrics().rmse());

    System.out.printf(
        Locale.ROOT,
        "%-12s %10.4f %10.4f%n",
        "Linear SVR",
        result.modelMetrics().mae(),
        result.modelMetrics().rmse());

    System.out.println("Model beats baseline: " + result.modelOutperformsBaseline());

    System.out.println("========================");
    System.out.println();
  }
}
