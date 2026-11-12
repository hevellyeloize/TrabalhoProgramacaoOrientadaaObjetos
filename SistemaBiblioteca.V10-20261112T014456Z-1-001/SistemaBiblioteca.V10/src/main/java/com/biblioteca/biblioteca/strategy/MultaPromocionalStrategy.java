package com.biblioteca.biblioteca.strategy;

public class MultaPromocionalStrategy implements CalculoMultaStrategy {
    @Override
    public double calcular(long diasAtraso) {
        return diasAtraso * 1.00;
    }
}