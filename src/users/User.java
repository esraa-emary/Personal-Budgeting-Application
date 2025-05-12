package users;

/**
 * Represents a user in the Personal Budgeting Application.
 * <p>
 * This class stores essential user information required for:
 * <ul>
 *   <li>Authentication (email and password)</li>
 *   <li>User identification (username)</li>
 * </ul>
 * <p>
 * User objects are created during registration and login processes and
 * passed to the authentication handlers.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see authentication.Login
 * @see authentication.Signup
 * @see authentication.Validation
 */
public class User {

    /**
     * User's display name
     */
    String username;

    /**
     * User's email address used for authentication
     */
    String email;

    /**
     * User's password
     */
    String password;

    /**
     * Creates a complete user with username, email, and password.
     * <p>
     * This constructor is typically used during user registration.
     *
     * @param username The display name for the user
     * @param email    The email address for the user (must be valid format)
     * @param password The password for the user account (must meet security requirements)
     * @see authentication.Validation#isValidEmail(String)
     * @see authentication.Validation#isValidPassword(String)
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Creates a user with only email and password.
     * <p>
     * This constructor is typically used during login when username
     * is not yet retrieved from the database.
     *
     * @param email    The email address for authentication
     * @param password The password for authentication
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.username = null;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's username.
     * <p>
     * May return null if the user was created with the email/password constructor.
     *
     * @return The username of the user, or null if not set
     */
    public String getusername() {
        return username;
    }
}