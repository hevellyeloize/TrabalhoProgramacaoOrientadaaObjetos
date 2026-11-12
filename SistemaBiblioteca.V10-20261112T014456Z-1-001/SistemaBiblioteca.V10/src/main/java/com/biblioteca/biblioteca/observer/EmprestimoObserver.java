package com.biblioteca.biblioteca.observer;

import com.biblioteca.biblioteca.model.Emprestimo;

public interface EmprestimoObserver {
    void atualizar(Emprestimo emprestimo);
}