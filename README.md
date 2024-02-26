# Automatização de API com RestAssured

O objetivo deste projeto é automatizar uma API pública (serverest.dev) utilizando o framework RestAssured.
Nele usei o Java 17, IntelliJ IDEA e as seguintes dependências:

- io.rest-assured
- org.junit.jupiter
- com.fasterxml.jackson.core
- com.google.guava
- org.projectlombok
- net.datafaker
- io.qameta.allure
- org.slf4j

## Configuração do Login

Em todas as classes de teste, dentro do método `Setup`, está configurado o login da seguinte forma:

@BeforeEach
public void setup() {
    baseURI = "http://localhost:3000";

    try {
        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("config.properties")).getPath();
        this.token = getToken(filePath, baseURI, "/login");

    } catch (IOException e) {
        e.printStackTrace();
    }
}

Este método realiza o login e obtém o token da API, permitindo que os demais testes sejam executados com sucesso. As credenciais de usuário e senha são armazenadas em um arquivo `config.properties` por questões de segurança. O tratamento de exceção está incluído para facilitar a configuração do login por qualquer pessoa que tenha interesse em clonar o projeto, são dois tratamentos principais: um para tratar o nome da pasta onde conta o projeto e outro para buscar automaticamente os valores dentro do arquivo config.properties.

## Métodos Auxiliares

Ao final de cada classe de teste, estão incluídos "métodos auxiliares". Estes, são chamados sempre que necessário para evitar a repetição de código.
