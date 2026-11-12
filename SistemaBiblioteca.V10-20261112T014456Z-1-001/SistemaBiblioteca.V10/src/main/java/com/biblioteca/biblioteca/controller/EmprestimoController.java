package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dao.LivroDAO;
import com.biblioteca.biblioteca.dao.UsuarioDAO;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.service.EmprestimoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class EmprestimoController {

    @FXML private ComboBox<Usuario> cbUsuario;
    @FXML private ComboBox<Livro> cbLivro;
    @FXML private DatePicker dpDataEmprestimo;
    @FXML private DatePicker dpDataDevolucao;
    @FXML private ComboBox<String> cbStatus;

    @FXML private TableView<Emprestimo> tabelaEmprestimos;
    @FXML private TableColumn<Emprestimo, Integer> colId;
    @FXML private TableColumn<Emprestimo, String> colUsuario;
    @FXML private TableColumn<Emprestimo, String> colLivro;
    @FXML private TableColumn<Emprestimo, LocalDate> colDataEmprestimo;
    @FXML private TableColumn<Emprestimo, LocalDate> colDataDevolucao;
    @FXML private TableColumn<Emprestimo, String> colStatus;

    private EmprestimoService emprestimoService = new EmprestimoService();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LivroDAO livroDAO = new LivroDAO();

    private ObservableList<Emprestimo> listaEmprestimos = FXCollections.observableArrayList();
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private ObservableList<Livro> listaLivros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDataEmprestimo.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));
        colDataDevolucao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colUsuario.setCellValueFactory(cellData -> {
            Usuario u = cellData.getValue().getUsuario();
            return new SimpleStringProperty(u != null ? u.getNome() : "");
        });

        colLivro.setCellValueFactory(cellData -> {
            Livro l = cellData.getValue().getLivro();
            return new SimpleStringProperty(l != null ? l.getTitulo() : "");
        });

        cbStatus.setItems(FXCollections.observableArrayList("Ativo", "Devolvido", "Atrasado"));

        carregarCombos();
        carregarTabela();

        tabelaEmprestimos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                cbUsuario.setValue(selecionado.getUsuario());
                cbLivro.setValue(selecionado.getLivro());
                dpDataEmprestimo.setValue(selecionado.getDataEmprestimo());
                dpDataDevolucao.setValue(selecionado.getDataDevolucao());
                cbStatus.setValue(selecionado.getStatus());
            }
        });
    }

    private void carregarCombos() {
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioDAO.listar());
        cbUsuario.setItems(listaUsuarios);

        listaLivros.clear();
        listaLivros.addAll(livroDAO.listar());
        cbLivro.setItems(listaLivros);
    }

    private void carregarTabela() {
        listaEmprestimos.clear();
        listaEmprestimos.addAll(emprestimoService.listar());
        tabelaEmprestimos.setItems(listaEmprestimos);
    }

    @FXML
    protected void onSalvarClick() {
        try {
            Emprestimo selecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();

            //Para o Service
            emprestimoService.salvar(
                    selecionado,
                    cbUsuario.getValue(),
                    cbLivro.getValue(),
                    dpDataEmprestimo.getValue(),
                    dpDataDevolucao.getValue(),
                    cbStatus.getValue()
            );

            mostrarAlerta("Sucesso", selecionado == null ? "Empréstimo registrado com sucesso!" : "Empréstimo atualizado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
            carregarTabela();
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Aviso", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar empréstimo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onExcluirClick() {
        Emprestimo selecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            emprestimoService.deletar(selecionado.getId());
            limparCampos();
            carregarTabela();
            mostrarAlerta("Sucesso", "Empréstimo excluído com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Aviso", "Selecione um empréstimo na tabela para excluir.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    protected void onLimparClick() {
        limparCampos();
    }

    private void limparCampos() {
        cbUsuario.setValue(null);
        cbLivro.setValue(null);
        dpDataEmprestimo.setValue(null);
        dpDataDevolucao.setValue(null);
        cbStatus.setValue(null);
        tabelaEmprestimos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String conteudo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}