package users;

public class User {

    String username;
    String email;
    String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.username = null;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getusername() {

        return username;
    }
}
