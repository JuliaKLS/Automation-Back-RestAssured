package dataFactory;

import model.Produto;
import model.ProdutoExistente;
import model.Usuario;
import net.datafaker.Faker;

import java.util.Locale;

public class ProdutoDataFactory {
    private static Faker faker = new Faker(new Locale("PT-BR"));

    public static Produto novoProduto(){

        Produto novoProduto = new Produto();

        novoProduto.setNome(faker.device().modelName() + "02");
        novoProduto.setPreco(faker.number().numberBetween(1,500));
        novoProduto.setDescricao(faker.toString());
        novoProduto.setQuantidade(faker.number().randomDigit());

        return novoProduto;
    }

    public static ProdutoExistente produtoExistente(String idProduto, int quantProduto){

        ProdutoExistente produtoExistente = new ProdutoExistente();

        produtoExistente.setIdProduto(idProduto);
        produtoExistente.setQuantidade(quantProduto);

        return produtoExistente;
    }
}
