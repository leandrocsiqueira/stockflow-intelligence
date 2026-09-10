package com.leandro.stockflowintelligence.ml;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DemandFeatureEngineer {

  private static final int WINDOW_SIZE = 7;

  public List<DemandFeature> generate(List<DailyDemand> series) {
    if (series == null || series.size() <= WINDOW_SIZE) {
      throw new IllegalArgumentException("At least 8 daily demand observations are required");
    }

    List<DailyDemand> ordered =
        series.stream().sorted(Comparator.comparing(demand -> demand.date())).toList();

    List<DemandFeature> features = new ArrayList<>();

    for (int i = WINDOW_SIZE; i < ordered.size(); i++) {
      DailyDemand current = ordered.get(i);

      double lag1 = ordered.get(i - 1).quantity();
      double lag7 = ordered.get(i - WINDOW_SIZE).quantity();

      double movingAverage7 = calculateMovingAverage(ordered, i);
      double standardDeviation7 = calculateStandardDeviation(ordered, i, movingAverage7);

      features.add(
          new DemandFeature(
              current.date(),
              current.date().getDayOfWeek().getValue(),
              lag1,
              lag7,
              movingAverage7,
              standardDeviation7,
              current.quantity()));
    }

    return List.copyOf(features);
  }

  private double calculateMovingAverage(List<DailyDemand> series, int currentIndex) {

    double sum = 0.0;

    for (int i = currentIndex - WINDOW_SIZE; i < currentIndex; i++) {
      sum += series.get(i).quantity();
    }

    return sum / WINDOW_SIZE;
  }

  private double calculateStandardDeviation(
      List<DailyDemand> series, int currentIndex, double mean) {

    double squaredDifferenceSum = 0.0;

    for (int i = currentIndex - WINDOW_SIZE; i < currentIndex; i++) {
      double difference = series.get(i).quantity() - mean;
      squaredDifferenceSum += difference * difference;
    }

    return Math.sqrt(squaredDifferenceSum / WINDOW_SIZE);
  }
}
