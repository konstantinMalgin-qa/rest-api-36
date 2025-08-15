import models.LoginBodyLombokModel;
import models.LoginResponseLombokModel;
import models.UnsuccessfulLoginResponseLobmokModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class LoginFirstVersionTests extends TestBase{


    @Test
    void fetchUsersTest() {
       given()
                .header("x-api-key", apiKey)
                .when()
                .get("/users")
               .then()
               .log().status()
               .log().body()
               .statusCode(200);
    }
    @Test
    void fetchUsersIdTest() {
        given()
                .header("x-api-key", apiKey)
                .when()
                .queryParam("page", "2")
                .get("/users")
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
                .get("/custom-endpoints/4")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }

    @Test
    void errorUsersIdTest() {
        given()
                .header("x-api-key", apiKey)
                .when()
                .get("/users/20")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void errorAithtorisationTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/login")

                .then()

                .log().status()
                .log().body()
                .statusCode(401)
                .body("error", is("Missing API key"));
    }

    @Test
    void successfulLoginTest() {
        //String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");
        LoginResponseLombokModel response = given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
                .header("x-api-key", apiKey)
        .when()
            .post("/login")

        .then()
       
            .log().status()
            .log().body()
            .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);
        assertNotNull(response.getToken());
    }

    @Test
    void errorRegisterTest() {
        //String authData = "{\"username\":\"Konstantin\", \"email\": \"konsta.holt@reqres.in\", \"password\": \"qazwsx\"}";

        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setName("Konstantin");
        authData.setEmail("konsta.holt@reqres.in");
        authData.setPassword("qazwsx");
        UnsuccessfulLoginResponseLobmokModel response =given()
                .body(authData)
                .contentType(JSON)
                .header("x-api-key", apiKey)
                .log().uri()
                .log().body()
                .log().headers()

                .when()
                .post("/register")

                .then()

                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(UnsuccessfulLoginResponseLobmokModel.class);
                assertEquals("Note: Only defined users succeed registration", response.getError());

    }

    @Test
    void unsuccessfulLogin400Test() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("");
        authData.setPassword("");
        UnsuccessfulLoginResponseLobmokModel response =
        given()
                .body(authData)
                .log().uri()
                .log().body()
                .log().headers()
                .header("x-api-key", apiKey)

        .when()
            .post("/login")

        .then()
            .log().status()
            .log().body()
            .statusCode(400)
                .extract()
                .as(UnsuccessfulLoginResponseLobmokModel.class);
        assertEquals("Missing email or username", response.getError());

    }

    @Test
    void userNotFoundTest() {

        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eveasdas.holt@reqres.in");
        authData.setPassword("cda");
        UnsuccessfulLoginResponseLobmokModel response =
        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
                .header("x-api-key", apiKey)

                .when()
                .post("/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract()
                .as(UnsuccessfulLoginResponseLobmokModel.class);
        assertEquals("user not found", response.getError());
         
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
                .post("/login")

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
                .post("/login")

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
                .post("/login")

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
                .post("/login")
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
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
