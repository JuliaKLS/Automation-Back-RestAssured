package dataFactory;

import model.Usuario;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class UsuarioDataFactory {
    private static Faker faker = new Faker(new Locale("PT-BR"));

    public static String admin() {
        String[] cargo = {
                "false",
                "true",
        };
        Random random = new Random();
        int indice = random.nextInt(cargo.length);
        return cargo[indice];
    }

    public static Usuario novoUsuario(){

        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome(faker.name().firstName());
        novoUsuario.setEmail(faker.internet().emailAddress());
        novoUsuario.setPassword(faker.passport().toString());
        novoUsuario.setAdministrador(admin());

        return novoUsuario;
    }
}
