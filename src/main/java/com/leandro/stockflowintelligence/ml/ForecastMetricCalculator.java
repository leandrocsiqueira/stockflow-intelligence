package com.leandro.stockflowintelligence.ml;

import org.springframework.stereotype.Component;
import smile.validation.metric.MAD;
import smile.validation.metric.RMSE;

@Component
public class ForecastMetricCalculator {

  public ForecastMetrics calculate(double[] actual, double[] predicted) {

    if (actual == null
        || predicted == null
        || actual.length == 0
        || actual.length != predicted.length) {

      throw new IllegalArgumentException(
          "Actual and predicted values must have the same non-zero length");
    }

    double mae = MAD.of(actual, predicted);
    double rmse = RMSE.of(actual, predicted);

    return new ForecastMetrics(mae, rmse);
  }
}
