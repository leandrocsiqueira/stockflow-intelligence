package com.leandro.stockflowintelligence.ml;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModelEvaluationService {

  private final DemandFeatureEngineer featureEngineer;
  private final TemporalTrainTestSplitter splitter;
  private final DemandRegressionTrainer trainer;
  private final ForecastMetricCalculator metricCalculator;

  public ModelEvaluationService(
      DemandFeatureEngineer featureEngineer,
      TemporalTrainTestSplitter splitter,
      DemandRegressionTrainer trainer,
      ForecastMetricCalculator metricCalculator) {

    this.featureEngineer = featureEngineer;
    this.splitter = splitter;
    this.trainer = trainer;
    this.metricCalculator = metricCalculator;
  }

  public ModelEvaluationResult evaluate(List<DailyDemand> series) {

    List<DemandFeature> features = featureEngineer.generate(series);

    TemporalDatasetSplit split = splitter.split(features);

    DemandRegressionModel model = trainer.train(split.training());

    double[] actual = split.testing().stream().mapToDouble(demand -> demand.target()).toArray();

    double[] baselinePredictions =
        split.testing().stream().mapToDouble(demand -> demand.movingAverage7()).toArray();

    double[] modelPredictions = split.testing().stream().mapToDouble(model::predict).toArray();

    ForecastMetrics baselineMetrics = metricCalculator.calculate(actual, baselinePredictions);

    ForecastMetrics modelMetrics = metricCalculator.calculate(actual, modelPredictions);

    return new ModelEvaluationResult(
        split.training().size(), split.testing().size(), baselineMetrics, modelMetrics);
  }
}
