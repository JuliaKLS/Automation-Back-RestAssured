import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static dataFactory.ProdutoDataFactory.novoProduto;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
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
                .log().all()
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
    public void testeConsultarProdutoPorId(){
        Response response = given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .post("/produtos")
        .then()
                .log().all()
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
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .put("/produtos/" + idProduto)
        .then()
                .log().all()
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
                .log().all()
                .header("Authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/produtos/" + idProduto)
        .then()
                .log().all()
                .body("_id", equalTo(idProduto))
                .extract().response();
    }

    public Response deleteProduto(String idProduto){
        return given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
       .when()
                .delete("/produtos/" + idProduto)
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"))
                .extract().response();
    }
}
