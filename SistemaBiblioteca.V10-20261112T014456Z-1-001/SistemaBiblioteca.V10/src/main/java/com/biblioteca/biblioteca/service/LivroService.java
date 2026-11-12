package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dao.LivroDAO;
import com.biblioteca.biblioteca.factory.factoryDAO;
import com.biblioteca.biblioteca.model.Livro;

import java.util.List;

public class LivroService {

    private LivroDAO livroDAO = factoryDAO.criarLivroDAO();
    public List<Livro> listar() {
        return livroDAO.listar();
    }

    public void salvar(Livro livroSelecionado, String titulo, String autor, String anoStr, String qtdStr) throws Exception {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }

        int ano;
        int quantidade;
        try {
            ano = Integer.parseInt(anoStr);
            quantidade = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Por favor, insira números válidos para Ano e Quantidade.");
        }

        if (ano < 0 || quantidade < 0) {
            throw new IllegalArgumentException("Ano e Quantidade não podem ser negativos.");
        }

        if (livroSelecionado == null) {
            Livro novoLivro = new Livro(0, titulo, autor, ano, quantidade);
            livroDAO.inserir(novoLivro);
        } else {
            livroSelecionado.setTitulo(titulo);
            livroSelecionado.setAutor(autor);
            livroSelecionado.setAno(ano);
            livroSelecionado.setQuantidade(quantidade);
            livroDAO.atualizar(livroSelecionado);
        }
    }

    public void deletar(int id) {
        livroDAO.deletar(id);
    }
}