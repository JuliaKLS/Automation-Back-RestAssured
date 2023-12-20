import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static dataFactory.UsuarioDataFactory.novoUsuario;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static util.TokenUtils.getToken;

public class UsuariosTest {
    private String token;

    @BeforeEach
    public void setup() {
        //baseURI = "https://serverest.dev";
        baseURI = "http://localhost:3000";


        try {
            String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("config.properties")).getPath();
            this.token = getToken(filePath, baseURI, "/login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarUsuariosCadastrados(){

        given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/usuarios")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testeCadastrarEditarUsuario(){
        Response response = given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .post("/usuarios")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idUsuario = response.jsonPath().getString("_id");

        given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .put("/usuarios/" + idUsuario)
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .delete("/usuarios/" + idUsuario)
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    public void testeBuscarUsuarioPorId(){
        Response response = given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .post("/usuarios")
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idUsuario = response.jsonPath().getString("_id");

        given()
                .log().all()
                .header("Authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/usuarios/" + idUsuario)
        .then()
                .log().all()
                .body("_id", equalTo(idUsuario));


        given()
                .log().all()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .delete("/usuarios/" + idUsuario)
        .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
}
