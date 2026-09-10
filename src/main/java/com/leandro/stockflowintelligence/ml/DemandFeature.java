package com.leandro.stockflowintelligence.ml;

import java.time.LocalDate;

public record DemandFeature(
    LocalDate date,
    double dayOfWeek,
    double lag1,
    double lag7,
    double movingAverage7,
    double standardDeviation7,
    double target) {

  public double[] predictors() {
    return new double[] {
      dayOfWeek,
      lag1,
      lag7,
      movingAverage7,
      standardDeviation7
    };
  }
}
