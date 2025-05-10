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


public class Signup {
    private static final String USERS_DB_FILE_PATH = "../files/users_db.json";
    User user;

    public Signup(User user) {
        this.user = user;
    }

    private void registerUser() {
        Gson
    }
    private String generateUniqueID(List<UserEntry> users) {

        int max_id = 0;
        for (UserEntry user : users) {

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

    private static class UserEntry {

        String id;
        String username;
        String email;
        String password;
        String createdAt;
        String lastLogin;
    }

    private static class UserDatabase {

        List<UserEntry> users;
    }
}


