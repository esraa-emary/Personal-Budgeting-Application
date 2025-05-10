import static run.Format.*;
import static run.Menu.*;

import run.Menu;
import authentication.*;
import users.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (isMain) {
            int choice = Menu.displayMainMenuAuthentication();
            Scanner external_input = new Scanner(System.in);
            String userName, password, confirmPassword, mail;
            switch (choice) {
                case 1:
                    System.out.print("Enter email address: ");
                    mail = external_input.nextLine();
                    System.out.print("Enter password: ");
                    password = external_input.nextLine();


                    break;

                case 2:
                    System.out.print("Enter username: ");
                    userName = external_input.nextLine();
                    System.out.print("Enter E-mail: ");
                    mail = external_input.nextLine();
                    System.out.print("Enter password: ");
                    password = external_input.nextLine();
                    System.out.print("Enter confirm password: ");
                    confirmPassword = external_input.nextLine();
                    User newUser = new User(userName, mail, password);
                    Signup signup = new Signup(newUser);


                    break;

                case 3:
                    System.out.println(Purple + Bold + "THANKS FOR USING OUR APPLICATION!" + Reset);
                    isMain = false;
                    break;

                default:
                    System.out.println(Red + Bold + "Invalid option, please try again\n" + Reset);
                    break;
            }
        }
    }
}