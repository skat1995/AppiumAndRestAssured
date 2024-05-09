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
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTestApi {
	private static final Logger logger = LogManager.getLogger(BaseTestApi.class);

	private static String baseUrl = "http://localhost:8080";
	private RequestSpecification req;
	protected String jsessionId;

	@BeforeTest
	public void setup() {
	}
	
	public RequestSpecification req() {
		return this.req(null);
	}
	
	public RequestSpecification req(String sessionId) {
		this.req = RestAssured.given().log().all().baseUri(baseUrl)
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.config(
						RestAssured.config().encoderConfig(
								EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)
						)
					)
				.redirects().follow(true).redirects().max(100) ;
		if(sessionId != null) {
			this.req.cookie("JSESSIONID", sessionId);
			logger.debug("Session id set as: " + sessionId);
		}
		return this.req;
	}

	@AfterClass // Runs after all test methods are finished
	public void tearDown() {
		// Add any cleanup logic if needed (e.g., closing resources)
	}
}