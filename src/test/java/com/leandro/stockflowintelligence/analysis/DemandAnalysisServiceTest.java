package com.leandro.stockflowintelligence.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.leandro.stockflowintelligence.data.StockMovementCsvReader;
import com.leandro.stockflowintelligence.forecast.MovingAverageBaseline;
import com.leandro.stockflowintelligence.statistics.DemandStatisticsCalculator;
import com.leandro.stockflowintelligence.timeseries.DailyDemandSeriesBuilder;
import org.junit.jupiter.api.Test;

class DemandAnalysisServiceTest {

  private final DemandAnalysisService service =
      new DemandAnalysisService(
          new StockMovementCsvReader(),
          new DailyDemandSeriesBuilder(),
          new DemandStatisticsCalculator(),
          new MovingAverageBaseline());

  @Test
  void shouldAnalyzeFixedDemandDataset() {
    DemandAnalysisResult result = service.analyze("data/stock-movements.csv", 1L);

    assertThat(result.productId()).isEqualTo(1L);
    assertThat(result.observations()).isEqualTo(14);
    assertThat(result.statistics().mean()).isCloseTo(16.3571428571, within(0.000001));
    assertThat(result.statistics().median()).isEqualTo(16.5);
    assertThat(result.statistics().variance()).isCloseTo(13.6318681319, within(0.000001));
    assertThat(result.statistics().standardDeviation()).isCloseTo(3.6921359850, within(0.000001));
    assertThat(result.baselineForecast()).isCloseTo(17.4285714286, within(0.000001));
  }
}
