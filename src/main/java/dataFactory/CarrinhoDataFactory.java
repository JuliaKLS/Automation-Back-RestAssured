package dataFactory;

import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import model.Carrinho;
import net.datafaker.Faker;

import java.util.List;
import java.util.Locale;

public class CarrinhoDataFactory{
    private static Faker faker = new Faker(new Locale("PT-BR"));

    public static Carrinho novoCarrinho() {
        Carrinho novoCarrinho = new Carrinho();
        novoCarrinho.setQuantidadeTotal(faker.number().numberBetween(1,5));
        return novoCarrinho;
    }
}


