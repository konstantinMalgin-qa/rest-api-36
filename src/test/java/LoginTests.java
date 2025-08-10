import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests {


    @Test
    void fetchUsersTest() {
        // Отправляем GET-запрос на получение списка пользователей
        Response response = given()
                .header("x-api-key", "reqres-free-v1") // добавляем хедеры
                .when()
                .get("https://reqres.in/api/users");

        // Проверяем статус-код и выводим тело ответа
        assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asString());
    }
    @Test
    void fetchUsersIdTest() {
        // Отправляем GET-запрос на получение списка пользователей
        Response response = given()
                .header("x-api-key", "reqres-free-v1") // добавляем хедеры
                .when()
                .get("https://reqres.in/api/users/5");

        // Проверяем статус-код и выводим тело ответа
        assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asString());
    }

    @Test
    void customEndpointsUsersIdTest() {
        Response response = given()
                .header("x-api-key", "reqres-free-v1")
                .log().uri()
                .when()
                .get("https://reqres.in/api/custom-endpoints/4");

        assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asString());
    }

    @Test
    void errorUsersIdTest() {
        // Отправляем GET-запрос на получение списка пользователей
        Response response = given()
                .header("x-api-key", "reqres-free-v1") // добавляем хедеры
                .when()
                .get("https://reqres.in/api/users/20");

        // Проверяем статус-код и выводим тело ответа
        assertEquals(response.getStatusCode(), 404);
        System.out.println(response.asString());
    }

    @Test
    void errorAithtorisationTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("https://reqres.in/api/login")

                .then()

                .log().status()
                .log().body()
                .statusCode(401)
                .body("error", is("Missing API key"));
    }

    @Test
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")
        .when()
            .post("https://reqres.in/api/login")

        .then()
       
            .log().status()
            .log().body()
            .statusCode(200)
            .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void errorRegisterTest() {
        String authData = "{\"username\":\"Konstantin\", \"email\": \"konsta.holt@reqres.in\", \"password\": \"qazwsx\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .post("https://reqres.in/api/register")

                .then()

                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    void unsuccessfulLogin400Test() {
        String authData = "";

        given()
                .body(authData)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")

        .when()
            .post("https://reqres.in/api/login")

        .then()
            .log().status()
            .log().body()
            .statusCode(400)
            .body("error", is("Missing email or username"));
    }

    @Test
    void userNotFoundTest() {
        String authData = "{\"email\": \"eveasdas.holt@reqres.in\", \"password\": \"cda\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .post("https://reqres.in/api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("user not found"));
    }

    @Test
    void missingPasswordTest() {
        String authData = "{\"email\": \"eveasdas.holt@reqres.in\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .post("https://reqres.in/api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }


    @Test
    void missingLoginTest() {
        String authData = "{\"password\": \"cda\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .post("https://reqres.in/api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }
    @Test
    void wrongBodyTest() {
        String authData = "%}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .post("https://reqres.in/api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void unsuccessfulLogin415Test() {
        given()
                .log().uri()
                .header("x-api-key", "reqres-free-v1")
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }
}
