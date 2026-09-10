package com.leandro.stockflowintelligence.ml;

import smile.regression.Regression;

public class DemandRegressionModel {

  private final Regression<double[]> model;

  public DemandRegressionModel(Regression<double[]> model) {
    this.model = model;
  }

  public double predict(DemandFeature feature) {
    return model.predict(feature.predictors());
  }
}
