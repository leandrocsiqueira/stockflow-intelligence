package com.leandro.stockflowintelligence.ml;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TemporalTrainTestSplitter {

  private static final double DEFAULT_TRAIN_RATIO = 0.8;

  public TemporalDatasetSplit split(List<DemandFeature> features) {
    return split(features, DEFAULT_TRAIN_RATIO);
  }

  public TemporalDatasetSplit split(List<DemandFeature> features, double trainRatio) {

    if (features == null || features.size() < 2) {
      throw new IllegalArgumentException("At least 2 feature observations are required");
    }

    if (trainRatio <= 0.0 || trainRatio >= 1.0) {
      throw new IllegalArgumentException("Train ratio must be greater than 0 and less than 1");
    }

    List<DemandFeature> ordered =
        features.stream().sorted(Comparator.comparing(demand -> demand.date())).toList();

    int splitIndex = (int) Math.floor(ordered.size() * trainRatio);

    if (splitIndex == 0) {
      splitIndex = 1;
    }

    if (splitIndex >= ordered.size()) {
      splitIndex = ordered.size() - 1;
    }

    List<DemandFeature> training = List.copyOf(ordered.subList(0, splitIndex));

    List<DemandFeature> testing = List.copyOf(ordered.subList(splitIndex, ordered.size()));

    return new TemporalDatasetSplit(training, testing);
  }
}
