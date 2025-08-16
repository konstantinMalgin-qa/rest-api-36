package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static tests.TestBase.apiKey;

public class ReqResSpec {
    public static RequestSpecification requestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .header("x-api-key", apiKey)
            .contentType(JSON);

    public static ResponseSpecification responseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(ALL)
                .build();
    }
    public static RequestSpecification errorRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON);
}