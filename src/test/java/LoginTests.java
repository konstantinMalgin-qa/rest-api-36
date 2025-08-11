import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests extends TestBase{


    @Test
    void fetchUsersTest() {
       given()
                .header("x-api-key", apiKey)
                .when()
                .get(baseURI + basePath +  "/users").then()
               .log().status()
               .log().body()
               .statusCode(200);
    }
    @Test
    void fetchUsersIdTest() {
        given()
                .header("x-api-key", apiKey)
                .when()
                .get(baseURI + basePath + "/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);

    }

    @Test
    void customEndpointsUsersIdTest() {
        given()
                .header("x-api-key", apiKey)
                .log().uri()
                .when()
                .get(baseURI + basePath + "/custom-endpoints/4")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }

    @Test
    void errorUsersIdTest() {
        Response response = given()
                .header("x-api-key", apiKey)
                .when()
                .get(baseURI + basePath + "/users/20");

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
                .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)
        .when()
            .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)
                .log().uri()

                .when()
                .post(baseURI + basePath + "/register")

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
                .header("x-api-key", apiKey)

        .when()
            .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)

                .when()
                .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)

                .when()
                .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)

                .when()
                .post(baseURI + basePath + "/login")

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
                .header("x-api-key", apiKey)

                .when()
                .post(baseURI + basePath + "/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void unsuccessfulLogin415Test() {
        given()
                .log().uri()
                .header("x-api-key", apiKey)
                .post(baseURI + basePath + "/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }
    @Test
    void successfulDeleteUserTest() {
        given()
                .header("x-api-key", apiKey)
                .contentType(JSON)
                .log().uri()
                .when()
                .delete(baseURI + basePath + "/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
