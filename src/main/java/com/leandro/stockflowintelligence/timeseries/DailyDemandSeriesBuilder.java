package com.leandro.stockflowintelligence.timeseries;

import com.leandro.stockflowintelligence.domain.StockMovement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

@Component
public class DailyDemandSeriesBuilder {
  public List<DailyDemand> build(List<StockMovement> movements, long productId) {
    Map<LocalDate, Integer> demandByDate = new TreeMap<>();

    movements.stream()
        .filter(movement -> movement.productId() == productId)
        .forEach(
            movement ->
                demandByDate.compute(
                    movement.date(),
                    (date, currentQuantity) ->
                        (currentQuantity == null ? 0 : currentQuantity) + movement.quantity()));

    return demandByDate.entrySet().stream()
        .map(entry -> new DailyDemand(productId, entry.getKey(), entry.getValue()))
        .toList();
  }
}
