package authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import users.User;
import dataStorage.*;

/**
 * Manages user registration for the Personal Budgeting Application.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Registering new users in the application</li>
 *   <li>Storing user credentials in a JSON database</li>
 *   <li>Generating unique user IDs</li>
 *   <li>Initializing a login session after successful registration</li>
 * </ul>
 * <p>
 * The class writes user information to a JSON database file and creates
 * the file if it doesn't exist.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Login
 * @see users.User
 * @see dataStorage.userDatabase
 * @see dataStorage.userEntry
 */
public class Signup {
    /**
     * Path to the JSON file storing user credentials and information
     */
    private static final String USERS_DB_FILE_PATH = "files/users_db.json";

    /**
     * The user object containing registration information
     */
    User user;

    /**
     * Login instance created after successful registration
     */
    public Login login;

    /**
     * Creates a signup instance and registers the provided user.
     * <p>
     * The constructor automatically registers the user in the database
     * and creates a login session for the newly registered user.
     *
     * @param user A User object containing registration information (username, email, password)
     * @see users.User
     * @see Login
     */
    public Signup(User user) {
        this.user = user;
        registerUser();
        login = new Login(user);
    }

    /**
     * Registers a new user in the application database.
     * <p>
     * This method:
     * <ol>
     *   <li>Creates or loads the user database from the JSON file</li>
     *   <li>Creates a new user entry with generated unique ID</li>
     *   <li>Sets initial user properties including timestamp and filename</li>
     *   <li>Adds the new user to the database</li>
     *   <li>Saves the updated database back to the JSON file</li>
     * </ol>
     * <p>
     * The user's data file identifier is created based on username and unique ID.
     */
    private void registerUser() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = new File(USERS_DB_FILE_PATH);
            dataStorage.userDatabase database;

            if (file.exists()) {
                FileReader reader = new FileReader(file);
                database = gson.fromJson(reader, dataStorage.userDatabase.class);
                reader.close();
            } else {
                database = new dataStorage.userDatabase();
                database.users = new ArrayList<>();
            }

            userEntry entry = new userEntry();
            entry.id = generateUniqueID(database.users);
            entry.filename = user.getusername().replace(" ", "") + entry.id;
            entry.username = user.getusername();
            entry.email = user.getEmail();
            entry.password = user.getPassword();
            entry.createdAt = getCurrentTime();
            entry.lastLogin = getCurrentTime();
            entry.current = true;

            database.users.add(entry);

            FileWriter writer = new FileWriter(file);
            gson.toJson(database, writer);
            writer.close();

            System.out.println("User resgistered successfully!");
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

    /**
     * Generates a unique numeric ID for new users.
     * <p>
     * This method determines the highest existing user ID in the database
     * and increments it by one to create a new unique identifier.
     *
     * @param users The list of existing user entries in the database
     * @return A string representation of the new unique ID
     */
    private String generateUniqueID(List<userEntry> users) {
        int max_id = 0;
        for (userEntry user : users) {
            try {
                int id = Integer.parseInt(user.id);
                if (id > max_id) {
                    max_id = id;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(max_id + 1);
    }

    /**
     * Gets the current system time as a string.
     * <p>
     * Used for recording registration and login timestamps in the user database.
     *
     * @return The current time as an ISO string
     */
    private String getCurrentTime() {
        return java.time.OffsetTime.now().toString();
    }
}