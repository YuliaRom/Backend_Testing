package MyTests;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class AlbumTests extends BaseTest{

    static String newAlbumHash = null;
    static String newImageId3 = null;
    static String newImageDeleteHash3 = null;
    static String newImageLink3 = null;

    @Order(1)
    @Test
    void albumCreationTest() {

        JsonPath response = given()
                .spec(requestSpecificationWithAuth)
                .formParam("title", "My Album")
                .formParam("description", "Some description")
                .formParam("privacy", "secret")
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response()
                .jsonPath();

        String albumHash = response.getString("data.id");

        newAlbumHash = albumHash;
    }

    @Order(2)
    @Test
    void getAlbumTest() {

        given()
                .spec(requestSpecificationWithAuth)
                .expect()
                .body("data.title", is(notNullValue()))
                .when()
                .get("https://api.imgur.com/3/album/" + newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);

    }

    @Order(3)
    @Test
    void uploadFile3Test() throws URISyntaxException {

        File file = new File(ImageTests.class.getResource("/wonder-day-skazochnyy-patrul-9-1536x1086.jpg").toURI());

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

        newImageId3 = imageId;
        newImageDeleteHash3 = imageDeleteHash;
        newImageLink3 = imageLink;

    }

    @Order(4)
    @Test
    void addImageToAnAlbumTest() {

        given()
                .spec(requestSpecificationWithAuth)
                .formParam("ids[]", newImageId3)
                .when()
                .post("https://api.imgur.com/3/album/{newAlbumHash}/add", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Order(5)
    @Test
    void getAlbumImagesTest() {

        given()
                .spec(requestSpecificationWithAuth)
                .formParam("ids[]", newImageId3)
                .when()
                .post("https://api.imgur.com/3/album/{newAlbumHash}/images", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);

    }

    @Order(6)
    @Test
    void removeImageFromAlbumTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .when()
                .post("https://api.imgur.com/3/album/{newAlbumHash}/remove_images", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Order(7)
    @Test

    void getAlbumImagesAfterRemovingTest() {

        given()
                .spec(requestSpecificationWithAuth)
                .formParam("ids[]", newImageId3)
                .when()
                .post("https://api.imgur.com/3/album/{newAlbumHash}/images", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);

    }

    @Order(8)
    @Test
    void noSelectImagesToAddTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .when()
                .post("https://api.imgur.com/3/album/{newAlbumHash}/add", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Order(9)
    @Test
    void albumDeletionTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .when()
                .delete("https://api.imgur.com/3/album/{newAlbumHash}", newAlbumHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Order(10)
    @Test
    void GetAlbumAfterDeleting() {
        given()
                .spec(requestSpecificationWithAuth)
                .expect()
                .body("data.title", is(notNullValue()))
                .when()
                .get("https://api.imgur.com/3/album/" + newAlbumHash)
                .prettyPeek()
                .then()
                .statusCode(404);

    }


}
