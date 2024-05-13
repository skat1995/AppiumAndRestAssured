package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



public class RestApiExampleAlternative {

    public static void main(String[] args) {
        try {
            // Make API request to fetch user data
            String userDataJson = fetchUserData();

            // Parse JSON response and display user information
            User user = parseUserData(userDataJson);

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
        // Define the API URL
        String apiUrl = "https://api.example.com/user-data";

        // Create a URL object
        URL url = new URL(apiUrl);

        // Open connection to the API URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // Close connection
        connection.disconnect();

        return response.toString();
    }

    private static User parseUserData(String userDataJson) {
        JSONObject jsonUser = new JSONObject(userDataJson);

        // Parse user information
        User user = new User();
        user.setName(jsonUser.getString("name"));

        // Parse posts
        JSONArray jsonPosts = jsonUser.getJSONArray("posts");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < jsonPosts.length(); i++) {
            JSONObject jsonPost = jsonPosts.getJSONObject(i);
            Post post = new Post();
            post.setTitle(jsonPost.getString("title"));
            post.setContent(jsonPost.getString("content"));
            posts.add(post);
        }
        user.setPosts(posts);

        return user;
    }

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
}

