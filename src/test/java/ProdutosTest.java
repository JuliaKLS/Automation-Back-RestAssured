import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Objects;
import static dataFactory.ProdutoDataFactory.novoProduto;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static util.TokenUtils.getToken;

public class ProdutosTest {
    private String token;

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

    @Test
    public void testListarProdutosCadastrados(){

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/produtos")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCadastrarProduto(){
        Response response = cadastrarProduto();

        String idProduto = response.jsonPath().getString("_id");

        deleteProduto(idProduto);
    }

    @Test
    public void testCadastrarProdutoSemToken(){
        given()
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .post("/produtos")
        .then()
                .log().all()
                .statusCode(401)
                .body("message",equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }

    @Test
    public void testCadastrarProdutoComNomeExistente(){
            // Suponha que você tenha um método novoProduto() que retorna um objeto JSON com o novo produto
            String novoProduto = "{"
                    + "\"nome\": \"OnePlus 6T\","
                    + "\"preco\": 277,"
                    + "\"descricao\": \"net.datafaker.Faker@4a2d66e\","
                    + "\"quantidade\": 4"
                    + "}";

            // Simulando o cenário onde o produto já existe

            // Realiza a criação do produto original
            given()
                    .header("authorization", this.token)
                    .contentType(ContentType.JSON)
                    .body(novoProduto)
                    .post("/produtos");

            // Agora, tenta criar um novo produto com o mesmo nome
            given()
                    .log().all()
                    .header("authorization", this.token)
                    .contentType(ContentType.JSON)
                    .body(novoProduto)
                    .post("/produtos")
                    .then()
                    .statusCode(400) // Verifica se o status code é 400 (Bad Request)
                    .body("message", equalTo("Já existe produto com esse nome")); // Verifica a mensagem de erro retornada

    }

    @Test
    public void testeConsultarProdutoPorId(){
        Response response = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .post("/produtos")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idProduto = response.jsonPath().getString("_id");

        consultaProduto(idProduto);
        deleteProduto(idProduto);

    }

    @Test
    public void testEditarProduto(){
        Response response = cadastrarProduto();
        String idProduto = response.jsonPath().getString("_id");

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .put("/produtos/" + idProduto)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        deleteProduto(idProduto);
    }

    //====================Métodos auxiliares para evitar repetição de código na classe.======================================================
    public Response cadastrarProduto() {
        return given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .post("/produtos")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();
    }

    public Response consultaProduto(String idProduto) {
        return given()
                .header("Authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/produtos/" + idProduto)
        .then()
                .body("_id", equalTo(idProduto))
                .extract().response();
    }

    public Response deleteProduto(String idProduto){
        return given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
       .when()
                .delete("/produtos/" + idProduto)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"))
                .extract().response();
    }
}

