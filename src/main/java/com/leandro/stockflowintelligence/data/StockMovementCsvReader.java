package com.leandro.stockflowintelligence.data;

import com.leandro.stockflowintelligence.domain.StockMovement;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class StockMovementCsvReader {
  public List<StockMovement> read(String path) {
    String validatedPath = Objects.requireNonNull(path, "Dataset path must not be null");
    ClassPathResource resource = new ClassPathResource(validatedPath);

    try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      Iterable<CSVRecord> records =
          CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(reader);
      List<StockMovement> movements = new ArrayList<>();

      for (CSVRecord record : records) {
        movements.add(
            new StockMovement(
                Long.parseLong(record.get("product_id")),
                LocalDate.parse(record.get("date")),
                Integer.parseInt(record.get("quantity"))));
      }

      return movements;
    } catch (IOException exception) {
      throw new IllegalStateException("Could not read stock movement dataset: " + path, exception);
    }
  }
}
