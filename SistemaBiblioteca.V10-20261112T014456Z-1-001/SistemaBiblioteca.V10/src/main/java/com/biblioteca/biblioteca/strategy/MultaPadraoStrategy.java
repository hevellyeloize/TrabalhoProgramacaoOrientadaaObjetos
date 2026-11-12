package com.biblioteca.biblioteca.strategy;

public class MultaPadraoStrategy implements CalculoMultaStrategy {
    @Override
    public double calcular(long diasAtraso) {
        return diasAtraso * 2.50;
    }
}