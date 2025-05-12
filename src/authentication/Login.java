package authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import users.User;
import dataStorage.*;

import javax.print.DocFlavor;

/**
 * Handles user authentication by validating user credentials against the stored database.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Authenticating users by email and password</li>
 *   <li>Managing login state</li>
 *   <li>Updating user's last login timestamp</li>
 *   <li>Providing access to authenticated user information</li>
 * </ul>
 * <p>
 * The class reads user information from a JSON database file and validates
 * the provided credentials against the stored data.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Signup
 * @see Validation
 * @see users.User
 */

public class Login {
    private static final String USERS_DB_FILE_PATH = "files/users_db.json";
    String mail, password;
    private boolean isLoggedIn = false;
    private userEntry loggedInUser = null;

    public Login(User user) {
        this.mail = user.getEmail();
        this.password = user.getPassword();
        this.isLoggedIn = authenticateUser();
    }


    /**
     * Performs the authentication process by validating credentials against the user database.
     * <p>
     * This method:
     * <ol>
     *   <li>Reads the user database from the JSON file</li>
     *   <li>Searches for matching email and password</li>
     *   <li>Updates the last login time if authentication succeeds</li>
     *   <li>Sets the current user flag appropriately</li>
     *   <li>Saves updates back to the database file</li>
     * </ol>
     *
     * @return true if authentication succeeds, false otherwise
     */

    private boolean authenticateUser() {
        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                System.out.println("No user database found, please register first");
                return false;
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            boolean found = false;
            for (userEntry user : database.users) {
                if (user.email.equals(this.mail) && user.password.equals(this.password)) {
                    user.lastLogin = getCurrentTime();
                    user.current = true;
                    this.loggedInUser = user;
                    found = true;

                    System.out.println("\nLogin successful: Welcome Back " + user.username);
                } else user.current = false;

                FileWriter writer = new FileWriter(file);
                gson.toJson(database, writer);
                writer.close();
            }
            if (found) return true;
            System.out.println("Invalid email or password, please try again or sign up first if you don't have an account");
            return false;
        } catch (Exception e) {
            System.err.println("Error during lgoin: " + e.getMessage());
            return false;
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public User getLoggedInUser() {
        if (loggedInUser == null) {
            return null;
        }
        return new User(loggedInUser.username, loggedInUser.password, loggedInUser.email);
    }

    public String getLoggedInUserFilename() {
        return loggedInUser != null ? loggedInUser.filename : null;
    }

    private String getCurrentTime() {
        return java.time.OffsetTime.now().toString();
    }
}


