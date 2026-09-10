package com.leandro.stockflowintelligence.ml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TemporalTrainTestSplitterTest {

  private final TemporalTrainTestSplitter splitter = new TemporalTrainTestSplitter();

  @Test
  void shouldSplitDatasetChronologically() {
    List<DemandFeature> features =
        List.of(feature(1), feature(2), feature(3), feature(4), feature(5));

    TemporalDatasetSplit split = splitter.split(features, 0.8);

    assertThat(split.training()).hasSize(4);
    assertThat(split.testing()).hasSize(1);

    assertThat(split.training().getFirst().date()).isEqualTo(LocalDate.of(2026, 8, 1));

    assertThat(split.training().getLast().date()).isEqualTo(LocalDate.of(2026, 8, 4));

    assertThat(split.testing().getFirst().date()).isEqualTo(LocalDate.of(2026, 8, 5));
  }

  @Test
  void shouldOrderFeaturesBeforeSplitting() {
    List<DemandFeature> features =
        List.of(feature(5), feature(2), feature(4), feature(1), feature(3));

    TemporalDatasetSplit split = splitter.split(features, 0.6);

    assertThat(split.training()).hasSize(3);
    assertThat(split.testing()).hasSize(2);

    assertThat(split.training())
        .extracting(demand -> demand.date())
        .containsExactly(
            LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), LocalDate.of(2026, 8, 3));

    assertThat(split.testing())
        .extracting(demand -> demand.date())
        .containsExactly(LocalDate.of(2026, 8, 4), LocalDate.of(2026, 8, 5));
  }

  @Test
  void shouldRejectInvalidTrainRatio() {
    List<DemandFeature> features = List.of(feature(1), feature(2), feature(3));

    assertThatThrownBy(() -> splitter.split(features, 1.0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Train ratio must be greater than 0 and less than 1");

    assertThatThrownBy(() -> splitter.split(features, 0.0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Train ratio must be greater than 0 and less than 1");
  }

  @Test
  void shouldRejectInsufficientObservations() {
    List<DemandFeature> features = List.of(feature(1));

    assertThatThrownBy(() -> splitter.split(features))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("At least 2 feature observations are required");
  }

  private DemandFeature feature(int day) {
    return new DemandFeature(LocalDate.of(2026, 8, day), 1.0, 10.0, 8.0, 9.0, 2.0, 11.0);
  }
}
