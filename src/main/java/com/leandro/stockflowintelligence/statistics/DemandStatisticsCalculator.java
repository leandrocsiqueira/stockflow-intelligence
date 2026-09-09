package com.leandro.stockflowintelligence.statistics;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.util.List;
import java.util.Objects;
import org.apache.commons.statistics.descriptive.Mean;
import org.apache.commons.statistics.descriptive.Median;
import org.apache.commons.statistics.descriptive.Variance;
import org.springframework.stereotype.Component;

@Component
public class DemandStatisticsCalculator {

  public DemandStatistics calculate(List<DailyDemand> series) {
    if (series == null || series.isEmpty()) {
      throw new IllegalArgumentException("Series must not be null or empty");
    }

    double[] values =
        series.stream()
            .mapToDouble(
                demand -> Objects.requireNonNull(demand, "DailyDemand must not be null").quantity())
            .toArray();

    double mean = Mean.of(values).getAsDouble();
    double median = Median.withDefaults().withCopy(true).evaluate(values);
    double variance = Variance.of(values).getAsDouble();
    double standardDeviation = Math.sqrt(variance);

    return new DemandStatistics(mean, median, variance, standardDeviation);
  }
}
