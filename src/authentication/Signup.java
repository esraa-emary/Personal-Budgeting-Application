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

public class Signup {
    private static final String USERS_DB_FILE_PATH = "files/users_db.json";
    User user;
    public Login login;

    public Signup(User user) {
        this.user = user;
        registerUser();
        login = new Login(user);
    }

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

    private String getCurrentTime() {

        return java.time.OffsetTime.now().toString();
    }
}

