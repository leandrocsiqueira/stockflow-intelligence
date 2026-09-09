package com.leandro.stockflowintelligence.analysis;

import com.leandro.stockflowintelligence.data.StockMovementCsvReader;
import com.leandro.stockflowintelligence.domain.StockMovement;
import com.leandro.stockflowintelligence.forecast.MovingAverageBaseline;
import com.leandro.stockflowintelligence.statistics.DemandStatistics;
import com.leandro.stockflowintelligence.statistics.DemandStatisticsCalculator;
import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import com.leandro.stockflowintelligence.timeseries.DailyDemandSeriesBuilder;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandAnalysisService {

  private final StockMovementCsvReader csvReader;
  private final DailyDemandSeriesBuilder seriesBuilder;
  private final DemandStatisticsCalculator statisticsCalculator;
  private final MovingAverageBaseline baseline;

  public DemandAnalysisService(
      StockMovementCsvReader csvReader,
      DailyDemandSeriesBuilder seriesBuilder,
      DemandStatisticsCalculator statisticsCalculator,
      MovingAverageBaseline baseline) {
    this.csvReader = csvReader;
    this.seriesBuilder = seriesBuilder;
    this.statisticsCalculator = statisticsCalculator;
    this.baseline = baseline;
  }

  public DemandAnalysisResult analyze(String datasetPath, long productId) {
    List<StockMovement> movements = csvReader.read(datasetPath);
    List<DailyDemand> series = seriesBuilder.build(movements, productId);
    DemandStatistics statistics = statisticsCalculator.calculate(series);
    double baselineForecast = baseline.forecast(series);

    return new DemandAnalysisResult(productId, series.size(), statistics, baselineForecast);
  }
}
