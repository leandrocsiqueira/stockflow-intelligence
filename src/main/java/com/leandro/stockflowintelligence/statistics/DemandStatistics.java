package com.leandro.stockflowintelligence.statistics;

public record DemandStatistics(
  double mean,
  double median,
  double variance,
  double standardDeviation
) {}
