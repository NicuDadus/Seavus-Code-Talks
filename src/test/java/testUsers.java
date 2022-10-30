import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class testUsers {

    private static final User user = new User();
    private static final String singleUserEndPoint = "api/users/2";
    private static final String createUserEndPoint = "api/users";


    @BeforeTest
    public void setUserInfo() {
        user.setName("John");
        user.setJob("Engineer");
    }

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .expectResponseTime(lessThan(5000L))
            .expectBody(notNullValue())
            .expectStatusCode(200)
            .build();

    @Test
    public void verifyUsingResponseSpecification() {
        given()
                .spec(testBaseClass.getRequestSpecification())
                .when()
                .get(singleUserEndPoint)
                .then()
                .spec(responseSpecification);
    }

    @Test
    public void verifyResponse() {
        given()
                .spec(testBaseClass.getRequestSpecification())
                .body(user)
                .when()
                .post(createUserEndPoint)
                .then()
                .statusCode(201)
                .body("name", equalTo(user.getName()))
                .body("job", equalTo(user.getJob()))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    public void verifyJsonSchema() {
        given()
                .spec(testBaseClass.getRequestSpecification())
                .when()
                .get(singleUserEndPoint)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("userSchema.json"));

    }

}

