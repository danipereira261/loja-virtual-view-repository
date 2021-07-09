package br.com.alura.jdbc.dao;

import br.com.alura.jdbc.modelo.Categoria;
import br.com.alura.jdbc.modelo.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection connection;

    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Categoria> listar() {
        try {
            List<Categoria> categorias = new ArrayList<>();
            String sql = "select id, nome from categoria";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        Categoria categoria = new Categoria(rst.getInt(1), rst.getString(2));

                        categorias.add(categoria);
                    }
                }
            }
            return categorias;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Categoria> listarComProduto() throws SQLException {
        Categoria ultima = null;
        List<Categoria> categorias = new ArrayList<>();

        String sql = "select c.id, c.nome, p.id, p.nome, p.descricao " + "from categoria c "
                + "inner join produto p on c.idD = p.categoria_id";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.execute();

            try (ResultSet rst = pstm.getResultSet()) {
                while (rst.next()) {
                    if (ultima == null || !ultima.getNome().equals(rst.getString(2))) {
                        Categoria categoria = new Categoria(rst.getInt(1), rst.getString(2));

                        categorias.add(categoria);
                        ultima = categoria;
                    }
                    Produto produto = new Produto(rst.getInt(3), rst.getString(4), rst.getString(5));
                    ultima.adicionar(produto);
                }
            }
            return categorias;
        }
    }
}
