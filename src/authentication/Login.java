package authentication;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import users.User;
import dataStorage.*;


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
    /**
     * Path to the JSON file storing user credentials and information
     */
    private static final String USERS_DB_FILE_PATH = "files/users_db.json";

    /**
     * The email address provided for authentication
     */
    String mail;

    /**
     * The password provided for authentication
     */
    String password;

    /**
     * Flag indicating whether authentication was successful
     */
    private boolean isLoggedIn = false;

    /**
     * Reference to the authenticated user's data if login is successful
     */
    private userEntry loggedInUser = null;

    /**
     * Creates a login instance and attempts to authenticate the provided user.
     * <p>
     * The constructor extracts email and password from the user object and
     * automatically attempts authentication upon creation.
     *
     * @param user A User object containing the email and password credentials
     * @see users.User
     */
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
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the login attempt was successful.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Retrieves the authenticated user's information as a User object.
     * <p>
     * This method creates a new User object with the authenticated user's
     * username, password, and email information.
     *
     * @return a User object with the authenticated user's information, or null if not logged in
     * @see users.User
     */
    public User getLoggedInUser() {
        if (loggedInUser == null) {
            return null;
        }
        return new User(loggedInUser.username, loggedInUser.password, loggedInUser.email);
    }

    /**
     * Gets the filename associated with the authenticated user's data file.
     * <p>
     * This filename is used to store and retrieve the user's personal budget data.
     *
     * @return the filename string for the authenticated user, or null if not logged in
     */
    public String getLoggedInUserFilename() {
        return loggedInUser != null ? loggedInUser.filename : null;
    }

    /**
     * Gets the current system time as a string.
     * <p>
     * Used for recording login timestamps in the user database.
     *
     * @return the current time as an ISO string
     */
    private String getCurrentTime() {
        return java.time.OffsetTime.now().toString();
    }
}