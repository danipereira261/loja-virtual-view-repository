package br.com.alura.jdbc.controller;

import br.com.alura.jdbc.dao.ProdutoDAO;
import br.com.alura.jdbc.modelo.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoController {

    public void deletar(Integer id) {
        System.out.println("Deletando produto");
    }

    public void salvar(Produto produto) {
        System.out.println("Salvando produto");

        try {
            ProdutoDAO produtoDAO = new ProdutoDAO();
            produtoDAO.salvar(produto);
        } catch (Exception e) {
        }
    }

    public List<Produto> listar() {
        List<Produto> produtos =
                new ArrayList<Produto>();
        produtos.add(new Produto("Nome do Produto de teste"
                , "Descrição do produto de teste"));
        return produtos;
    }

    public void alterar(String nome, String descricao, Integer id) {
        System.out.println("Alterando produto");
    }
}
