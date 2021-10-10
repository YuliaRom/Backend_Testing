package MyTests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static java.lang.System.getProperties;
import static org.junit.jupiter.api.Assertions.*;

    public class AccountTests {
        static Map<String, String> headers = new HashMap<>();
        static Properties properties = new Properties();
        static String token;
        static String username;

        @BeforeAll

        static void setUp() {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            RestAssured.filters(new AllureRestAssured());
            getProperties();
            token = properties.getProperty("token");
            username = properties.getProperty("username");
            headers.put("Authorization", "Bearer 6494ed30670f2ce7acf7d59f8417f64223ebbd03");

        }


        @Test
        void getAccountInfoTest() {
        JsonPath response = given()
                .headers(headers)
                .when()
                .get("https://api.imgur.com/3/account/juliaromaniko3")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String CheckUrl = response.getString("data.url");
        assertEquals("juliaromaniko3", CheckUrl);

    }



}

