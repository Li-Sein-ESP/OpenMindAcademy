package services;

import entities.User;
import okhttp3.*;
import org.json.JSONObject;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionService {

    private static final String BASE_URL = "http://127.0.0.1:5000";
    private static final OkHttpClient client = new OkHttpClient();
    private Connection connection;

    public FaceRecognitionService() {
        connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            throw new IllegalStateException("Failed to initialize FaceRecognitionService: Database connection is null");
        }
    }

    // Save face encoding for a user during registration
    public String saveFace(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty for face capture.");
        }

        // Prepare JSON payload
        JSONObject json = new JSONObject();
        json.put("email", email);

        // Create HTTP request to Flask server
        RequestBody body = RequestBody.create(
                json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/faceid/register")
                .post(body)
                .build();

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Failed to capture face: " + response.code() + " - " + response.message());
            }

            // Parse response
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.has("error")) {
                throw new Exception("Face capture failed: " + jsonResponse.getString("error"));
            }

            // Extract face encoding
            String faceEncoding = jsonResponse.getJSONArray("encoding").toString();
            updateFaceEncoding(email, faceEncoding); // Update database with the encoding
            return faceEncoding;
        } catch (IOException e) {
            throw new Exception("Error communicating with face recognition server: " + e.getMessage());
        }
    }

    // Update the user's face encoding in the database
    public void updateFaceEncoding(String email, String faceEncoding) throws SQLException {
        String query = "UPDATE users SET face_encoding = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, faceEncoding);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    // Recognize a face and return the matching user
    public User recognizeFace(UserService userService) throws Exception {
        // Create HTTP request to Flask server for face recognition
        Request request = new Request.Builder()
                .url(BASE_URL + "/faceid/login")
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Face recognition failed: " + response.code() + " - " + response.message());
            }

            // Parse response
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.has("error")) {
                throw new Exception("Face recognition failed: " + jsonResponse.getString("error"));
            }

            // Extract matched email
            String matchedEmail = jsonResponse.getString("email");
            if (matchedEmail == null || matchedEmail.isEmpty()) {
                return null;
            }
            return userService.getUserByEmail(matchedEmail);
        } catch (IOException e) {
            throw new Exception("Error communicating with face recognition server: " + e.getMessage());
        }
    }
}