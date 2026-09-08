package com.leandro.stockflowintelligence.domain;

import java.time.LocalDate;

public record StockMovement(long productId, LocalDate date, int quantity) {}
