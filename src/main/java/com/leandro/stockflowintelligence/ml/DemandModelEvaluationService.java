package com.leandro.stockflowintelligence.ml;

import com.leandro.stockflowintelligence.data.StockMovementCsvReader;
import com.leandro.stockflowintelligence.domain.StockMovement;
import com.leandro.stockflowintelligence.timeseries.DailyDemand;
import com.leandro.stockflowintelligence.timeseries.DailyDemandSeriesBuilder;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DemandModelEvaluationService {

  private final StockMovementCsvReader csvReader;
  private final DailyDemandSeriesBuilder seriesBuilder;
  private final ModelEvaluationService evaluationService;

  public DemandModelEvaluationService(
      StockMovementCsvReader csvReader,
      DailyDemandSeriesBuilder seriesBuilder,
      ModelEvaluationService evaluationService) {

    this.csvReader = csvReader;
    this.seriesBuilder = seriesBuilder;
    this.evaluationService = evaluationService;
  }

  public ModelEvaluationResult evaluate(String datasetPath, long productId) {

    List<StockMovement> movements = csvReader.read(datasetPath);

    List<DailyDemand> series = seriesBuilder.build(movements, productId);

    if (series.isEmpty()) {
      throw new IllegalArgumentException("No demand observations found for product: " + productId);
    }

    return evaluationService.evaluate(series);
  }
}
