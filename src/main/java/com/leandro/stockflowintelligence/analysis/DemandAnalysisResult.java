package com.leandro.stockflowintelligence.analysis;

import com.leandro.stockflowintelligence.statistics.DemandStatistics;

public record DemandAnalysisResult(
    long productId, int observations, DemandStatistics statistics, double baselineForecast) {}
