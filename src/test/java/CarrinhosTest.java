import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ProdutoExistente;
import model.Value;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static dataFactory.ProdutoDataFactory.novoProduto;
import static dataFactory.ProdutoDataFactory.produtoExistente;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static util.TokenUtils.getToken;

public class CarrinhosTest {
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
    public void testListarCarrinhosCadastrados(){

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/carrinhos")
        .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCadastrarCarrinho(){

        int qtdItensCarrinho = 4;
        List<ProdutoExistente> listaProdutos = new ArrayList<>();

        while (listaProdutos.size() < qtdItensCarrinho){
            Response responseCadastro = cadastrarProduto();
            String idProduto = responseCadastro.jsonPath().getString("_id");

            Response responseConsulta = consultaProduto(idProduto);
            int quantProduto = responseConsulta.jsonPath().getInt("quantidade");

            if(quantProduto > 0){
                listaProdutos.add(produtoExistente(idProduto, quantProduto));
            }
        }

        Value value = new Value();
        value.setProdutos(listaProdutos);

        Response responseCarrinho = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(value)
        .when()
                .post("/carrinhos")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idCarrinho = responseCarrinho.jsonPath().getString("_id");

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/carrinhos/" + idCarrinho)
        .then()
                .statusCode(HttpStatus.SC_OK);

        excluirCarrinhoRetornarProdutoEstoque();
    }

    @Test
    public void testBuscarCarrinhoPorId(){
        int qtdItensCarrinho = 3;
        List<ProdutoExistente> listaProdutos = new ArrayList<>();

        while (listaProdutos.size() < qtdItensCarrinho){
            Response responseCadastro = cadastrarProduto();
            String idProduto = responseCadastro.jsonPath().getString("_id");

            Response responseConsulta = consultaProduto(idProduto);
            int quantProduto = responseConsulta.jsonPath().getInt("quantidade");

            if(quantProduto > 0){
                listaProdutos.add(produtoExistente(idProduto, quantProduto));
            }
        }

        Value value = new Value();
        value.setProdutos(listaProdutos);

        Response responseCarrinho = given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(value)
        .when()
                .post("/carrinhos")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        String idCarrinho = responseCarrinho.jsonPath().getString("_id");

        given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .get("/carrinhos/" + idCarrinho)
        .then()
                .statusCode(HttpStatus.SC_OK);

        excluirCarrinho();
    }

    //====================Métodos auxiliares para evitar repetição de código na classe.======================================================
    public Response cadastrarProduto() {
        return given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
                .body(novoProduto())
        .when()
                .post("/produtos")
        .then()
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

    public Response excluirCarrinho(){
        return given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .delete("/carrinhos/concluir-compra" )
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message",equalTo("Registro excluído com sucesso"))
                .extract().response();
    }

    public Response excluirCarrinhoRetornarProdutoEstoque(){
        return given()
                .header("authorization", this.token)
                .contentType(ContentType.JSON)
        .when()
                .delete("/carrinhos/cancelar-compra" )
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message",equalTo("Registro excluído com sucesso. Estoque dos produtos reabastecido"))
                .extract().response();
    }
}
