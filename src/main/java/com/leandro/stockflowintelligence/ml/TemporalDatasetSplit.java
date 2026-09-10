package com.leandro.stockflowintelligence.ml;

import java.util.List;

public record TemporalDatasetSplit(List<DemandFeature> training, List<DemandFeature> testing) {}
