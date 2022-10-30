import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class testBaseClass {

    private static final String userName = "someUserName";
    private static final String password = "somePassword";


    private static final String baseURI = "https://reqres.in/";
    private static final String authenticationEndPoint = "loginEndPoint";
    private static final AuthenticationUser authenticationUser = new AuthenticationUser();

    protected static String getAccessToken(String username, String password) {
        authenticationUser.setUsername(username);
        authenticationUser.setPassword(password);
        return given()
                .contentType(ContentType.JSON)
                .body(authenticationUser)
                .when()
                .post(baseURI + authenticationEndPoint)
                .then()
                .extract()
                .path("tokenJsonPath").toString();
    }


    private static RequestSpecification requestSpecification = getInitializedSpecification();

    private static RequestSpecification getInitializedSpecification() {
        PreemptiveOAuth2HeaderScheme preemptiveOAuth2HeaderScheme = new PreemptiveOAuth2HeaderScheme();
        preemptiveOAuth2HeaderScheme.setAccessToken(getAccessToken(userName, password));
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setContentType(ContentType.JSON)
                .setAuth(preemptiveOAuth2HeaderScheme)
                .build();
        return requestSpecification;
    }


    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

}
