package com.biblioteca.biblioteca.factory;

import com.biblioteca.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.biblioteca.dao.LivroDAO;
import com.biblioteca.biblioteca.dao.MultaDAO;
import com.biblioteca.biblioteca.dao.UsuarioDAO;

public class factoryDAO {

    public static LivroDAO criarLivroDAO() {
        return new LivroDAO();
    }

    public static UsuarioDAO criarUsuarioDAO() {
        return new UsuarioDAO();
    }

    public static EmprestimoDAO criarEmprestimoDAO() {
        return new EmprestimoDAO();
    }

    public static MultaDAO criarMultaDAO() {
        return new MultaDAO();
    }
}