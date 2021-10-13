package MyTests;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

    public class AccountTests extends BaseTest{

        @Test
        void getAccountInfoTest() {
            JsonPath response = given()
                    .spec(requestSpecificationWithAuth)
                    .when()
                    .get("https://api.imgur.com/3/account/" + username)
                    .then()
                    .spec(positiveResponseSpecification)
                    .extract()
                    .response()
                    .jsonPath();

        }


        @Test
            void getAccountSettingTest() {
            given()
                    .spec(requestSpecificationWithAuth)
                    .expect()
                    .body("data.account_url", is("juliaromaniko3"))
                    .body("data.email", is("julia.romaniko@gmail.com"))
                    .when()
                    .get("https://api.imgur.com/3/account/me/settings")
                    .prettyPeek()
                    .then()
                    .spec(positiveResponseSpecification);

            }

        @Test

            void changeAccountSettingsTest() {
            given()
                    .spec(requestSpecificationWithAuth)
                    .formParam("album_privacy", "secret")
                    .when()
                    .put("https://api.imgur.com/3/account/juliaromaniko3/settings")
                    .prettyPeek()
                    .then()
                    .spec(positiveResponseSpecification);
        }
    }





