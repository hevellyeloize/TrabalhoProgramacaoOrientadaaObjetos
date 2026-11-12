package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dao.MultaDAO;
import com.biblioteca.biblioteca.factory.factoryDAO;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Multa;

import java.util.List;

public class MultaService {

    private MultaDAO multaDAO = factoryDAO.criarMultaDAO();

    public List<Multa> listar() {
        return multaDAO.listar();
    }

    public void salvar(Multa multaSelecionada, Emprestimo emprestimo, String valorStr, String statusStr) throws Exception {
        if (emprestimo == null || statusStr == null || valorStr == null || valorStr.isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos obrigatórios (Empréstimo, Valor e Status).");
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Por favor, insira um valor numérico válido para a multa.");
        }

        if (valor < 0) {
            throw new IllegalArgumentException("O valor da multa não pode ser negativo.");
        }

        boolean paga = statusStr.equalsIgnoreCase("Paga");

        if (multaSelecionada == null) {
            Multa novaMulta = new Multa(0, emprestimo, valor, paga);
            multaDAO.inserir(novaMulta);
        } else {
            multaSelecionada.setEmprestimo(emprestimo);
            multaSelecionada.setValor(valor);
            multaSelecionada.setPaga(paga);
            multaDAO.atualizar(multaSelecionada);
        }
    }

    public void deletar(int id) {
        multaDAO.deletar(id);
    }
}