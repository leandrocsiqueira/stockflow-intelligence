package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class ForecastMetricCalculatorTest {

  private final ForecastMetricCalculator calculator = new ForecastMetricCalculator();

  @Test
  void shouldCalculateMaeAndRmse() {
    double[] actual = {10.0, 12.0, 14.0};
    double[] predicted = {11.0, 11.0, 16.0};

    ForecastMetrics metrics = calculator.calculate(actual, predicted);

    assertThat(metrics.mae()).isCloseTo(1.3333333333, within(0.000001));

    assertThat(metrics.rmse()).isCloseTo(Math.sqrt(2.0), within(0.000001));
  }

  @Test
  void shouldRejectArraysWithDifferentSizes() {
    double[] actual = {10.0, 12.0};
    double[] predicted = {11.0};

    assertThatThrownBy(() -> calculator.calculate(actual, predicted))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Actual and predicted values must have the same non-zero length");
  }

  @Test
  void shouldRejectEmptyArrays() {
    assertThatThrownBy(() -> calculator.calculate(new double[0], new double[0]))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Actual and predicted values must have the same non-zero length");
  }
}
