package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.biblioteca.dao.MultaDAO;
import com.biblioteca.biblioteca.factory.factoryDAO;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Multa;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.observer.EmprestimoObserver;
import com.biblioteca.biblioteca.observer.NotificacaoEmprestimoObserver;
import com.biblioteca.biblioteca.strategy.CalculoMultaStrategy;
import com.biblioteca.biblioteca.strategy.MultaPadraoStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoService {

    private EmprestimoDAO emprestimoDAO = factoryDAO.criarEmprestimoDAO();
    private MultaDAO multaDAO = factoryDAO.criarMultaDAO();

    private List<EmprestimoObserver> observadores = new ArrayList<>();

    public EmprestimoService() {
        this.adicionarObservador(new NotificacaoEmprestimoObserver());
    }

    public void adicionarObservador(EmprestimoObserver observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores(Emprestimo emprestimo) {
        for (EmprestimoObserver observador : observadores) {
            observador.atualizar(emprestimo);
        }
    }

    public List<Emprestimo> listar() {
        return emprestimoDAO.listar();
    }

    public void salvar(Emprestimo emprestimoSelecionado, Usuario usuario, Livro livro, LocalDate dataEmprestimo, LocalDate dataDevolucao, String status) throws Exception {
        if (usuario == null || livro == null || dataEmprestimo == null || status == null) {
            throw new IllegalArgumentException("Preencha todos os campos obrigatórios (Usuário, Livro, Data Empréstimo e Status).");
        }

        if (dataDevolucao != null && dataDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A data de devolução não pode ser anterior à data do empréstimo.");
        }

        Emprestimo emprestimoAfetado;

        if (emprestimoSelecionado == null) {
            emprestimoAfetado = new Emprestimo(0, livro, usuario, dataEmprestimo, dataDevolucao, status);
            emprestimoDAO.inserir(emprestimoAfetado);

            if ("Atrasado".equalsIgnoreCase(status)) {
                gerarMultaAutomatica(emprestimoAfetado);
            }
        } else {
            emprestimoSelecionado.setUsuario(usuario);
            emprestimoSelecionado.setLivro(livro);
            emprestimoSelecionado.setDataEmprestimo(dataEmprestimo);
            emprestimoSelecionado.setDataDevolucao(dataDevolucao);
            emprestimoSelecionado.setStatus(status);
            emprestimoDAO.atualizar(emprestimoSelecionado);

            emprestimoAfetado = emprestimoSelecionado;

            if ("Atrasado".equalsIgnoreCase(status)) {
                gerarMultaAutomatica(emprestimoSelecionado);
            }
        }

        notificarObservadores(emprestimoAfetado);
    }

    public void deletar(int id) {
        emprestimoDAO.deletar(id);
    }

    public void gerarMultaAutomatica(Emprestimo emprestimo) {
        if (emprestimo == null) return;

        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = emprestimo.getDataDevolucao() != null ? emprestimo.getDataDevolucao() : hoje;

        long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(dataPrevista, hoje);
        if (diasAtraso <= 0) diasAtraso = 1;

        CalculoMultaStrategy estrategia = new MultaPadraoStrategy();
        double valorCalculado = estrategia.calcular(diasAtraso);

        Multa multa = new Multa();
        multa.setEmprestimo(emprestimo);
        multa.setPaga(false);
        multa.setValor(valorCalculado);

        multaDAO.inserir(multa);
    }
}