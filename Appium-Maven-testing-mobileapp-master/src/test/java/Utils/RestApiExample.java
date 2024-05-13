package Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestApiExample {

    public static void main(String[] args) {
        try {
            // Make API request to fetch user data
            String userDataJson = fetchUserData();

            // Parse JSON response and deserialize it
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Post.class, new CustomPostDeserializer());
            objectMapper.registerModule(module);
            User user = objectMapper.readValue(userDataJson, User.class);

            // Display user information
            System.out.println("User Name: " + user.getName());
            System.out.println("Posts:");
            for (Post post : user.getPosts()) {
                System.out.println("Title: " + post.getTitle());
                System.out.println("Content: " + post.getContent());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fetchUserData() throws IOException {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Define the API URL
        String apiUrl = "https://api.example.com/user-data";

        // Create a GET request to the API URL
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String name;
        private List<Post> posts;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }
    }

    public static class Post {
        private String title;
        private String content;

        // Getters and setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class CustomPostDeserializer extends JsonDeserializer<Post> {
        @Override
        public Post deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            // Read JSON object and map it to the Post class
            return mapper.readValue(jp, Post.class);
        }
    }
}
