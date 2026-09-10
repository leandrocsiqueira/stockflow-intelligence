package com.leandro.stockflowintelligence.ml;

import java.util.List;
import org.springframework.stereotype.Component;
import smile.regression.Regression;
import smile.regression.SVM;

@Component
public class DemandRegressionTrainer {

  private static final double EPSILON = 0.1;
  private static final double C = 10.0;

  public DemandRegressionModel train(
      List<DemandFeature> trainingFeatures) {

    if (trainingFeatures == null || trainingFeatures.size() < 2) {
      throw new IllegalArgumentException(
          "At least 2 training observations are required");
    }

    double[][] predictors =
        trainingFeatures.stream()
            .map(feature -> feature.predictors())
            .toArray(double[][]::new);

    double[] targets =
        trainingFeatures.stream()
        .mapToDouble(feature -> feature.target())
            .toArray();

    Regression<double[]> model =
        SVM.fit(
            predictors,
            targets,
            new SVM.Options(EPSILON, C));

    return new DemandRegressionModel(model);
  }
}
