package tests;

import models.LoginBodyLombokModel;
import models.LoginResponseLombokModel;
import models.UnsuccessfulLoginResponseLobmokModel;
import models.UsersResponseLombokModel;
import org.junit.jupiter.api.Test;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.ReqResSpec.errorRequestSpec;
import static specs.ReqResSpec.requestSpec;
import static specs.ReqResSpec.responseSpec;

public class LoginTests extends TestBase {

    String
            supportUrl = "https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral",
            supportText = "Tired of writing endless social media content? Let Content Caddy generate it for you.";

    @Test
    void successfulSingleUserTest() {
        UsersResponseLombokModel response = step("Make request", ()->
                given(requestSpec)
                        .get("/users/2")
                        .then()
                        .spec(responseSpec(200))
                        .extract()
                        .as(UsersResponseLombokModel.class));

        step("Check response", () -> {
            assertEquals(2, response.getData().getId());
            assertEquals("janet.weaver@reqres.in", response.getData().getEmail());
            assertEquals("Janet", response.getData().getFirst_name());
            assertEquals("Weaver", response.getData().getLast_name());
            assertEquals(baseURI + "/img/faces/2-image.jpg", response.getData().getAvatar());
            assertEquals(supportUrl, response.getSupport().getUrl());
            assertEquals(supportText, response.getSupport().getText());
        });
    }

    @Test
    void errorAithtorisationTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");
        UnsuccessfulLoginResponseLobmokModel response = step("Make request", ()->

                given(errorRequestSpec)

                .body(authData)

                .when()
                .post("/login")

                .then()

                        .spec(responseSpec(401))
                .extract().as(UnsuccessfulLoginResponseLobmokModel.class));
        step("Check response", () ->
            assertEquals("Missing API key", response.getError())
        );

    }

    @Test
    void successfulLoginTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");
        LoginResponseLombokModel response = step("Make request", ()->
                given(requestSpec)
                .body(authData)

        .when()
            .post("/login")

        .then()
                        .spec(responseSpec(200))

                .extract()
                        .as(LoginResponseLombokModel.class));
        step("Check response", () ->
        assertNotNull(response.getToken())
        );
    }

    @Test
    void errorRegisterTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setName("Konstantin");
        authData.setEmail("konsta.holt@reqres.in");
        authData.setPassword("qazwsx");
        UnsuccessfulLoginResponseLobmokModel response = step("Make request", ()->
                given(requestSpec)
                .body(authData)
                        .when()
                .post("/register")

                .then()

                        .spec(responseSpec(400))
                        .extract()
                        .as(UnsuccessfulLoginResponseLobmokModel.class)
        );

        step("Check response", () ->
                assertEquals("Note: Only defined users succeed registration", response.getError()));

    }

    @Test
    void unsuccessfulLogin400Test() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("");
        authData.setPassword("");
        UnsuccessfulLoginResponseLobmokModel response = step("Make request", ()->
        given()
                .body(authData)
                .log().uri()
                .log().body()
                .log().headers()
                .header("x-api-key", TestBase.apiKey)

        .when()
            .post("/login")

        .then()
            .log().status()
            .log().body()
            .statusCode(400)
                .extract()
                .as(UnsuccessfulLoginResponseLobmokModel.class));
        step("Check response", () ->
        assertEquals("Missing email or username", response.getError()));

    }

    @Test
    void userNotFoundTest() {

        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eveasdas.holt@reqres.in");
        authData.setPassword("cda");
        UnsuccessfulLoginResponseLobmokModel response = step("Make request", ()->
        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
                .header("x-api-key", TestBase.apiKey)

                .when()
                .post("/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract()
                .as(UnsuccessfulLoginResponseLobmokModel.class));
        step("Check response", () ->
        assertEquals("user not found", response.getError()));

    }


    @Test
    void successfulDeleteUserTest() {
        step("Make request", ()->
        given(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpec(204))
        );
    }
}
