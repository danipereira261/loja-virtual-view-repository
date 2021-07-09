package br.com.alura.jdbc.dao;

import br.com.alura.jdbc.factory.ConnectionFactory;
import br.com.alura.jdbc.modelo.Categoria;
import br.com.alura.jdbc.modelo.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Connection connection;

    public ProdutoDAO() {
        this.connection = new ConnectionFactory().recuperarConexao();
    }

    public void salvar(Produto produto) throws SQLException {
        String sql = "insert into produto (nome, descricao, categoria_id) values (?, ?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, produto.getNome());
            pstm.setString(2, produto.getDescricao());
            pstm.setInt(3, produto.getCategoriaId());

            pstm.execute();

            try (ResultSet rst = pstm.getGeneratedKeys()) {
                while (rst.next()) {
                    produto.setId(rst.getInt(1));
                }
            }
        }
    }

    public List<Produto> listar() throws SQLException {
        List<Produto> produtos = new ArrayList<Produto>();
        String sql = "select id, nome, descricao from produto";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.execute();

            trasformarResultSetEmProduto(produtos, pstm);
        }
        return produtos;
    }

    public List<Produto> buscar(Categoria ct) throws SQLException {
        List<Produto> produtos = new ArrayList<Produto>();
        String sql = "select id, nome, descricao from produto where categoria_id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, ct.getId());
            pstm.execute();

            trasformarResultSetEmProduto(produtos, pstm);
        }
        return produtos;
    }

    public void deletar(Integer id) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement("delete from produto where = ?")) {
            stm.setInt(1, id);
            stm.execute();
        }
    }

    public void alterar(String nome, String descricao, Integer id) throws SQLException {
        try (PreparedStatement stm = connection
                .prepareStatement("update produto p set p.nome = ?, p.descricao = ? where id = ?")) {
            stm.setString(1, nome);
            stm.setString(2, descricao);
            stm.setInt(3, id);
            stm.execute();
        }
    }

    private void trasformarResultSetEmProduto(List<Produto> produtos, PreparedStatement pstm) throws SQLException {
        try (ResultSet rst = pstm.getResultSet()) {
            while (rst.next()) {
                Produto produto = new Produto(rst.getInt(1), rst.getString(2), rst.getString(3));

                produtos.add(produto);
            }
        }
    }
}
