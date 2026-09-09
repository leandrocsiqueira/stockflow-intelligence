package com.leandro.stockflowintelligence.forecast;

import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MovingAverageBaseline {
  private static final int WINDOW_SIZE = 7;

  public double forecast(List<DailyDemand> series) {
    if (series == null || series.size() < WINDOW_SIZE) {
      throw new IllegalArgumentException("At least 7 daily demand observations are required");
    }
    return series.stream()
        .sorted(Comparator.comparing((DailyDemand demand) -> demand.date()).reversed())
        .limit(WINDOW_SIZE)
        .mapToInt(demand -> demand.quantity())
        .average()
        .orElseThrow();
  }
}
