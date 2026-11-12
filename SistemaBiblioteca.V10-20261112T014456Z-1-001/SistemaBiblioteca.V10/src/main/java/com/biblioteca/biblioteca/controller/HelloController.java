package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.service.LivroService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAno;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtBusca;

    @FXML private TableView<Livro> tabelaLivros;
    @FXML private TableColumn<Livro, Integer> colId;
    @FXML private TableColumn<Livro, String> colTitulo;
    @FXML private TableColumn<Livro, String> colAutor;
    @FXML private TableColumn<Livro, Integer> colAno;
    @FXML private TableColumn<Livro, Integer> colQuantidade;

    private LivroService livroService = new LivroService();
    private ObservableList<Livro> listaLivros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        carregarTabela();

        FilteredList<Livro> dadosFiltrados = new FilteredList<>(listaLivros, b -> true);

        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(livro -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (livro.getTitulo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (livro.getAutor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Livro> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabelaLivros.comparatorProperty());

        tabelaLivros.setItems(dadosOrdenados);

        tabelaLivros.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtTitulo.setText(selecionado.getTitulo());
                txtAutor.setText(selecionado.getAutor());
                txtAno.setText(String.valueOf(selecionado.getAno()));
                txtQuantidade.setText(String.valueOf(selecionado.getQuantidade()));
            }
        });
    }

    private void carregarTabela() {
        listaLivros.clear();
        listaLivros.addAll(livroService.listar());
    }

    @FXML
    protected void onSalvarClick() {
        try {
            Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem();

            //Para o Service
            livroService.salvar(
                    livroSelecionado,
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    txtAno.getText(),
                    txtQuantidade.getText()
            );

            mostrarAlerta("Sucesso", livroSelecionado == null ? "Livro cadastrado com sucesso!" : "Livro atualizado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
            carregarTabela();
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Aviso / Validação", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar livro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onExcluirClick() {
        Livro selecionado = tabelaLivros.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            livroService.deletar(selecionado.getId());
            limparCampos();
            carregarTabela();
            mostrarAlerta("Sucesso", "Livro excluído com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Aviso", "Selecione um livro na tabela para excluir.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    protected void onLimparClick() {
        limparCampos();
    }

    private void limparCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAno.clear();
        txtQuantidade.clear();
        tabelaLivros.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String conteudo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}