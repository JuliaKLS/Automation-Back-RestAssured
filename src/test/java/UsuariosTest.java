import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Usuario;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Objects;

import static dataFactory.UsuarioDataFactory.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static util.TokenUtils.getToken;

public class UsuariosTest {
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
    public void testListarUsuariosCadastrados(){

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCadastrarUsuario(){
        Response responseCadastroUsuario = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idUsuario = responseCadastroUsuario.jsonPath().getString("_id");
        buscarUsuario(idUsuario);
        deleteUsuario(idUsuario);
    }

    @Test
    public void testBuscarUsuarioPorId(){
        Response response = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idUsuario = response.jsonPath().getString("_id");

        buscarUsuario(idUsuario);
        deleteUsuario(idUsuario);
    }

    @Test
    public void testEditarUsuario(){
        Response response = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idUsuario = response.jsonPath().getString("_id");

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoUsuario())
        .when()
                .put("/usuarios/" + idUsuario)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        deleteUsuario(idUsuario);
    }

    @Test
    public void testValidarEmail(){
        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(usuarioMesmoEmail())
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message",equalTo("Este email já está sendo usado"));
    }

    @Test
    public void testBuscarUsuarioInexistente(){

        given()
                .header("Authorization", this.token)
                .contentType(ContentType.JSON)
         .when()
                .get("/usuarios/" + "0123456789")
         .then()
                .body("message",equalTo("Usuário não encontrado"));

    }

//====================Métodos auxiliares para evitar repetição de código na classe.======================================================

    public Response deleteUsuario(String idUsuario){
        return given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .delete("/usuarios/" + idUsuario)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"))
                .extract().response();
    }

    public Response buscarUsuario(String idUsuario){
        return given()
                .header("Authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/usuarios/" + idUsuario)
        .then()
                .body("_id", equalTo(idUsuario))
                .extract().response();
    }
}
