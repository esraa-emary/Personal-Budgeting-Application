import static run.Format.*;
import static run.Menu.*;

import income.Budget;

import run.Menu;
import authentication.*;
import users.User;

import java.util.Scanner;

public class Main {
    public static void subMenus(String filename) {
        Scanner sub_menu_input = new Scanner(System.in);
        int choice, innerChoice;

//        System.out.println("\nEnter the name of the file to create with the proper extension: ");
        Budget bt = new Budget(filename);

        choice = Menu.displayMainMenuSections();
        while (choice != 3) {
            switch (choice) {
                case 1:
                    innerChoice = Menu.displayMainMenuIncome();
                    while (innerChoice != 12) {
                        System.out.print("\n");
                        optionsIncome(innerChoice, bt, filename, sub_menu_input);
                        innerChoice = Menu.displayMainMenuIncome();
                    }
                    break;

                case 2:
                    innerChoice = Menu.displayMainMenuPayment();
                    while (innerChoice != 5) {
                        System.out.print("\n");
                        optionsPayment(innerChoice, bt, filename, sub_menu_input);
                        innerChoice = Menu.displayMainMenuPayment();
                    }
                    break;

                case 3:
                    System.out.println(Purple + Bold + "THANKS FOR USING OUR APPLICATION!" + Reset);
                    isMain = false;
                    break;

                default:
                    System.out.println(Red + Bold + "Invalid option, please try again\n" + Reset);
                    break;
            }
            choice = Menu.displayMainMenuSections();
        }
    }

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
                    User user = new User(mail,password);
                    Login login = new Login(user);
                    subMenus(login.getLoggedInUserFilename());
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
                    subMenus(signup.login.getLoggedInUserFilename());
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