package MyTests;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import java.io.File;
import java.net.URISyntaxException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

    public class ImageTests extends BaseTest{

        static String newImageId = null;
        static String newImageDeleteHash = null;
        static String newImageLink = null;
        static String newImageId2 = null;
        static String newImageDeleteHash2 = null;
        static String newImageLink2 = null;


    @Order(1)
    @Test
    void uploadFileTest() throws URISyntaxException {

        File file = new File(ImageTests.class.getResource("/d6826a08b0ef65cc695233bc31a31d1a.png").toURI());

        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .multiPart("image", file)
                .multiPart("name", "PawPatrol")
                .expect()
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();


        String imageId = response.getString("data.id");
        String imageLink = response.getString("data.link");
        String imageDeleteHash = response.getString("data.deletehash");

        newImageId = imageId;
        newImageDeleteHash = imageDeleteHash;
        newImageLink = imageLink;

    }

    @Order(2)
    @Test
    void uploadFile2Test() throws URISyntaxException {

        File file = new File(ImageTests.class.getResource("/pj-masks-coloring-pages-to-print-luxury-pj-masks-to-for-free-pj-masks-kids-coloring-pages-of-pj-masks-coloring-pages-to-print.jpg").toURI());

        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .multiPart("image", file)
                .multiPart("name", "PJ Masks")
                .expect()
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();

        String imageId = response.getString("data.id");
        String imageLink = response.getString("data.link");
        String imageDeleteHash = response.getString("data.deletehash");

        newImageId2 = imageId;
        newImageDeleteHash2 = imageDeleteHash;
        newImageLink2 = imageLink;

    }

    @Order(3)
    @Test
    void getImageTest(){
        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.id", is(newImageId))
                .when()
                .get("https://api.imgur.com/3/image/" + newImageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();
}

    @Order(4)
    @Test
        void uploadWithoutFileTest()  {

            given()
                    .spec(requestSpecificationWithAuth)
                    .when()
                    .post("https://api.imgur.com/3/upload")
                    .prettyPeek()
                    .then()
                    .spec(negativeResponseSpecification400)
                    .extract()
                    .response()
                    .jsonPath();

        }

    @Order(5)
    @Test
    void updateInformationTest() {
        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .multiPart("name", "NewNameFromJava")
                .expect()
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/" + newImageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();
    }

    @Order(6)
    @Test
        void favoriteImageTest() {
        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .expect()
                .body("data", is("favorited"))
                .when()
                .post("https://api.imgur.com/3/image/" + newImageId + "/favorite")
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();
    }

    @Order(7)
    @Test
        void unFavoriteImageTest() {
            JsonPath response = given()
                    .spec(requestSpecificationWithAuth)
                    .expect()
                    .body("data", is("unfavorited"))
                    .when()
                    .post("https://api.imgur.com/3/image/" + newImageId + "/favorite")
                    .prettyPeek()
                    .then()
                    .extract()
                    .response()
                    .jsonPath();
        }

    @Order(8)
    @Test
        void deleteImageNoAuthTest(){
            JsonPath response = given()
                    .spec(requestSpecificationWithAuth)
                    .expect()
                    .body("data", is(true))
                    .when()
                    .delete("https://api.imgur.com/3/image/" + newImageDeleteHash)
                    .prettyPeek()
                    .then()
                    .spec(positiveResponseSpecification)
                    .extract()
                    .response()
                    .jsonPath();
        }

    @Order(9)
    @Test
    void deleteImageAuthTest(){
        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .expect()
                .body("data", is(true))
                .when()
                .delete("https://api.imgur.com/3/image/" + newImageId2)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();
    }

    @Order(10)
    @Test
        void getImageAfterDeletingTest() {
            JsonPath response = given()
                    .spec(requestSpecificationWithAuth)
                    .expect()
                    .when()
                    .get("https://api.imgur.com/3/image/" + newImageId)
                    .prettyPeek()
                    .then()
                    .statusCode(404)
                    .extract()
                    .response()
                    .jsonPath();
        }

}



