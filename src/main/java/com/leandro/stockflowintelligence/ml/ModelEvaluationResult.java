package com.leandro.stockflowintelligence.ml;

public record ModelEvaluationResult(
    int trainingObservations,
    int testingObservations,
    ForecastMetrics baselineMetrics,
    ForecastMetrics modelMetrics) {

  public boolean modelOutperformsBaseline() {
    return modelMetrics.mae() < baselineMetrics.mae()
        && modelMetrics.rmse() < baselineMetrics.rmse();
  }
}
