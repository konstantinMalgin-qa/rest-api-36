import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class LoginTests extends TestBase{


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
                .get("/users?page=2")
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
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

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
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

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
            .statusCode(200)
            .body("token", notNullValue());
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
                .post("/register")

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
            .post("/login")

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
                .post("/login")

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
