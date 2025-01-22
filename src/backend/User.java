package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class User {
    private String username;
    private String password;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validate credentials directly from the users.txt file.
     * @param inputUsername The username input.
     * @param inputPassword The password input.
     * @return True if credentials match, false otherwise.
     */
    public static boolean validateFromFile(String inputUsername, String inputPassword) {
        File file = new File("users.txt");
        if (!file.exists()) {
            return false; // If the file doesn't exist, no users are registered yet.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    if (storedUsername.equals(inputUsername) && storedPassword.equals(inputPassword)) {
                        return true; // Credentials match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // No match found
    }
}