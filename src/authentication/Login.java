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

            for (userEntry user : database.users) {

                if (user.email.equals(this.mail) && user.password.equals(this.password)) {

                    user.lastLogin = getCurrentTime();
                    this.loggedInUser = user;

                    FileWriter writer = new FileWriter(file);
                    gson.toJson(database, writer);
                    writer.close();

                    System.out.println("Login successful: Welcome Back" + user.username);
                    return true;
                }
            }
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

    public String getLoggedInUserFilename(){

        return loggedInUser != null ? loggedInUser.filename : null;
    }

    private String getCurrentTime() {

        return java.time.OffsetTime.now().toString();
    }


}


