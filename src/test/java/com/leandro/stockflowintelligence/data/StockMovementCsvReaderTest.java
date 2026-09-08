package com.leandro.stockflowintelligence.data;

import com.leandro.stockflowintelligence.domain.StockMovement;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StockMovementCsvReaderTest {
    private final StockMovementCsvReader reader = new StockMovementCsvReader();

    @Test
    void shouldReadStockMovementsFromCsv() {
        List<StockMovement> movements =
                reader.read("data/stock-movements.csv");

        assertThat(movements).hasSize(14);
        assertThat(movements.getFirst())
                .isEqualTo(new StockMovement(
                        1L,
                        LocalDate.of(2026, 8, 1),
                        12
                ));
        assertThat(movements.getLast())
                .isEqualTo(new StockMovement(
                        1L,
                        LocalDate.of(2026, 8, 14),
                        23
                ));
    }
}
