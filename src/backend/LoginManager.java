package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginManager {

    private static final String USERS_FILE = "users.txt";

    // Constructor
    public LoginManager() {
        // No need for in-memory user storage if file-based validation is used
    }

    // Register a new user
    public void registerUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getUsername() + "," + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Authenticate user credentials from the file
    public boolean authenticate(String username, String password) {
        return User.validateFromFile(username, password);
    }

    // Retrieve a user object by username (if needed for additional functionalities)
    public User getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return new User(parts[0], parts[1]); // Return the matched user
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found
    }

    // Check if a user is already registered
    public boolean isUserRegistered(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true; // Return true if username exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Return false if username not found
    }
}
