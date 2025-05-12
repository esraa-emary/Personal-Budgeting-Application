package dataStorage;

/**
 * Represents a single user record in the database for the Personal Budgeting Application.
 * <p>
 * This class serves as a data structure that stores all relevant user information including:
 * <ul>
 *   <li>User identification (ID and filename)</li>
 *   <li>Authentication details (username, email, password)</li>
 *   <li>Timestamp information (account creation, last login)</li>
 *   <li>Session state (current user flag)</li>
 * </ul>
 * <p>
 * Instances of this class are serialized to/from JSON format when stored in the database file.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see userDatabase
 * @see authentication.Login
 * @see authentication.Signup
 */
public class userEntry {

    /**
     * Unique identifier for the user
     */
    public String id;

    /**
     * Filename associated with this user's budget data storage
     */
    public String filename;

    /**
     * User's display name
     */
    public String username;

    /**
     * User's email address (used for authentication)
     */
    public String email;

    /**
     * User's password (stored in plain text - would need encryption in production)
     */
    public String password;

    /**
     * Timestamp of when the user account was created
     */
    public String createdAt;

    /**
     * Timestamp of the user's most recent login
     */
    public String lastLogin;

    /**
     * Flag indicating if this is the currently active user
     */
    public boolean current;
}