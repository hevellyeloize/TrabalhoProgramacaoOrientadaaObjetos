package com.biblioteca.biblioteca.observer;

import com.biblioteca.biblioteca.model.Emprestimo;

public class NotificacaoEmprestimoObserver implements EmprestimoObserver {
    @Override
    public void atualizar(Emprestimo emprestimo) {
        System.out.println("[NOTIFICAÇÃO] O status do empréstimo do livro '" +
                emprestimo.getLivro().getTitulo() +
                "' para o usuário " + emprestimo.getUsuario().getNome() +
                " foi atualizado para: " + emprestimo.getStatus());
    }
}