package com.leandro.stockflowintelligence.timeseries;

import java.time.LocalDate;

public record DailyDemand(long productId, LocalDate date, int quantity) {}
