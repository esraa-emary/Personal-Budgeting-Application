package dataStorage;

import java.util.List;

/**
 * Represents the collection of all users registered in the Personal Budgeting Application.
 * <p>
 * This class serves as a container for user entries and is designed to be:
 * <ul>
 *   <li>Serialized to JSON for persistent storage</li>
 *   <li>Deserialized from JSON when loading the application</li>
 * </ul>
 * <p>
 * The database structure is intentionally simple, containing just a list of user entries.
 * This design facilitates easy serialization and deserialization using the Gson library.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see userEntry
 */
public class userDatabase {

    /**
     * List containing all registered user entries
     */
    public List<userEntry> users;
}