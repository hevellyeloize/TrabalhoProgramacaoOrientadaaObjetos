package com.biblioteca.biblioteca.strategy;

import java.time.LocalDate;

public interface CalculoMultaStrategy {
    double calcular(long diasAtraso);
}