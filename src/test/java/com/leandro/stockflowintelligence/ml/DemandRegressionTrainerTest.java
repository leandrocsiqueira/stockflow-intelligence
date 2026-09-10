package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DemandRegressionTrainerTest {

  private final DemandRegressionTrainer trainer = new DemandRegressionTrainer();

  @Test
  void shouldTrainModelAndProducePrediction() {
    List<DemandFeature> training =
        List.of(
            feature(1, 10, 12),
            feature(2, 12, 14),
            feature(3, 14, 16),
            feature(4, 16, 18),
            feature(5, 18, 20),
            feature(6, 20, 22));

    DemandRegressionModel model = trainer.train(training);

    DemandFeature observation = feature(7, 22, 24);

    double prediction = model.predict(observation);

    assertThat(prediction).isFinite();
    assertThat(prediction).isPositive();
  }

  private DemandFeature feature(int day, double lag1, double target) {

    return new DemandFeature(LocalDate.of(2026, 8, day), day, lag1, 10.0, lag1 - 1.0, 2.0, target);
  }
}
