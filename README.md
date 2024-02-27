# Testes automatizados de API com RestAssured

[![Badge ServeRest](https://img.shields.io/badge/API-ServeRest-green)](https://github.com/ServeRest/ServeRest/)

O objetivo deste projeto é realizar testes automatizados em uma API pública (serverest.dev) através do framework RestAssured.
Nele utilizei a linguagem de programação Java na versão 17, como IDE o IntelliJ IDEA e as seguintes depêndencias:
 
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

Este método realiza o login e obtém o token da API, permitindo que os demais testes sejam executados com sucesso. As credenciais de usuário e senha são armazenadas em um arquivo `config.properties` por questões de segurança, para executar este projeto basta você buscar o usuário e senha na API serverest.dev.

## Métodos Auxiliares

Ao final de cada classe de teste, estão incluídos "métodos auxiliares". Estes, foram criados para permitir o reuso de código dentro do projeto.

## Ambiente

Desenvolvi meus testes de carga localmente através do Docker. 
