package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static tests.TestBase.apiKey;

public class ErrorRequestSpec {
    public static RequestSpecification errorRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON);
}