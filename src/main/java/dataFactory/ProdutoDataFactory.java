package dataFactory;

import model.Produto;
import model.Usuario;
import net.datafaker.Faker;

import java.util.Locale;

public class ProdutoDataFactory {
    private static Faker faker = new Faker(new Locale("PT-BR"));

    public static Produto novoProduto(){

        Produto novoProduto = new Produto();

        novoProduto.setNome(faker.device().modelName());
        novoProduto.setPreco(faker.number().numberBetween(1,500));
        novoProduto.setDescricao(faker.toString());
        novoProduto.setQuantidade(faker.number().randomDigit());

        return novoProduto;
    }
}
