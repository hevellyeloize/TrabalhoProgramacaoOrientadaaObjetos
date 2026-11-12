package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dao.UsuarioDAO;
import com.biblioteca.biblioteca.factory.factoryDAO;
import com.biblioteca.biblioteca.model.Usuario;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = factoryDAO.criarUsuarioDAO();

    public List<Usuario> listar() {
        return usuarioDAO.listar();
    }

    public void salvar(Usuario usuarioSelecionado, String nome, String email, String telefone) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }

        if (usuarioSelecionado == null) {
            Usuario novoUsuario = new Usuario(0, nome, email, telefone);
            usuarioDAO.inserir(novoUsuario);
        } else {
            usuarioSelecionado.setNome(nome);
            usuarioSelecionado.setEmail(email);
            usuarioSelecionado.setTelefone(telefone);
            usuarioDAO.atualizar(usuarioSelecionado);
        }
    }

    public void deletar(int id) {
        usuarioDAO.deletar(id);
    }
}