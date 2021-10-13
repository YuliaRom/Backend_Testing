package MyTests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.hamcrest.Matchers.*;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;

    static ResponseSpecification positiveResponseSpecification;
    static RequestSpecification requestSpecificationWithAuth;
    static ResponseSpecification negativeResponseSpecification400;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");

       positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectResponseTime(lessThanOrEqualTo(5000L))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

       negativeResponseSpecification400 = new ResponseSpecBuilder()
               .expectBody("status", equalTo(400))
               .expectBody("success", is(false))
               .expectResponseTime(lessThanOrEqualTo(5000L))
               .expectContentType(ContentType.JSON)
               .expectStatusCode(400)
               .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 6494ed30670f2ce7acf7d59f8417f64223ebbd03")
                .build();

            }

    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}