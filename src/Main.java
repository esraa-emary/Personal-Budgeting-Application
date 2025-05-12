import static authentication.Validation.isValidEmail;
import static authentication.Validation.isValidPassword;
import static run.Format.*;
import static run.Menu.*;

import Transactions.TransactionController;
import Transactions.TransactionService;
import income.Budget;

import run.Menu;
import authentication.*;
import users.User;

import java.util.Scanner;


/**
 * Main entry point for the Personal Budgeting Application.
 * <p>
 * This class serves as the primary controller for the application, handling:
 * <ul>
 *   <li>User authentication (login and signup)</li>
 *   <li>Main menu navigation and selection</li>
 *   <li>Submenu management for income and payment operations</li>
 *   <li>Input validation for user credentials</li>
 * </ul>
 * <p>
 * The application flow starts with authentication, then proceeds to various
 * budget management options organized into income and payment categories.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see authentication.Login
 * @see authentication.Signup
 * @see income.Budget
 * @see Transactions.TransactionController
 */
public class Main {

    /**
     * Manages the submenu system after successful user authentication.
     * <p>
     * This method loads the user's budget data from their personal file and
     * presents two main category options:
     * <ul>
     *   <li>Income management - for tracking income, savings, goals and reminders</li>
     *   <li>Payment management - for tracking debts, donations and transactions</li>
     * </ul>
     * Each category contains its own set of operations that are handled by
     * delegating to the appropriate methods in the {@link run.Menu} class.
     *
     * @param filename The data file identifier associated with the authenticated user
     * @see run.Menu#optionsIncome
     * @see run.Menu#optionsPayment
     */

    public static void subMenus(String filename) {
        Scanner sub_menu_input = new Scanner(System.in);
        int choice, innerChoice;
        Budget bt = new Budget(filename);
        TransactionService ts = new TransactionService();
        TransactionController tc = new TransactionController(ts);
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
                    while (innerChoice != 7) {
                        System.out.print("\n");
                        optionsPayment(innerChoice, bt, filename, sub_menu_input, tc);
                        innerChoice = Menu.displayMainMenuPayment();
                    }
                    break;

                default:
                    System.out.println(Red + Bold + "Invalid option, please try again\n" + Reset);
                    break;
            }
            choice = Menu.displayMainMenuSections();
        }
    }

    /**
     * Application entry point that controls the authentication flow and main program loop.
     * <p>
     * This method displays the authentication menu with options to:
     * <ul>
     *   <li>Login with existing credentials</li>
     *   <li>Sign up as a new user</li>
     *   <li>Exit the application</li>
     * </ul>
     * <p>
     * For login, it validates the email and password against stored credentials.
     * For signup, it ensures the email follows a standard format and the password
     * meets minimum security requirements before creating a new user account.
     * <p>
     * Upon successful authentication, it transitions to the submenu system for
     * budget management operations.
     *
     * @param args Command line arguments (not used)
     * @see authentication.Validation#isValidEmail
     * @see authentication.Validation#isValidPassword
     * @see authentication.Login
     * @see authentication.Signup
     */

    public static void main(String[] args) {
        int choice = Menu.displayMainMenuAuthentication();
        Scanner external_input = new Scanner(System.in);
        while (choice != 3) {
            String userName, password, confirmPassword, mail;
            switch (choice) {
                case 1:
                    System.out.print("Enter email address: ");
                    mail = external_input.nextLine();
                    System.out.print("Enter password: ");
                    password = external_input.nextLine();
                    User user = new User(mail, password);
                    Login login = new Login(user);
                    if (login.isLoggedIn()) {
                        subMenus(login.getLoggedInUserFilename());
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    userName = external_input.nextLine();
                    System.out.print("Enter E-mail: ");
                    mail = external_input.nextLine();
                    while (!isValidEmail(mail)) {
                        System.out.println(Red + Bold + "Invalid e-mail address, please follow the format email@example.com" + Reset);
                        System.out.print(Bold + "Enter E-mail: " + Reset);
                        mail = external_input.nextLine();
                    }
                    System.out.print("Enter password: ");
                    password = external_input.nextLine();
                    System.out.print("Enter confirm password: ");
                    confirmPassword = external_input.nextLine();

                    while (!confirmPassword.equals(password) || !isValidPassword(password)) {

                        if (!confirmPassword.equals(password) && !isValidPassword(password)) {

                            System.out.println(Red + Bold + "Password must be at least 6 characters, and passwords need to match!" + Reset);
                            System.out.print(Bold + "Enter password: " + Reset);
                            password = external_input.nextLine();
                            System.out.print(Bold + "Enter confirm password: " + Reset);
                            confirmPassword = external_input.nextLine();
                        } else if (!isValidPassword(password)) {
                            System.out.println(Red + Bold + "Password must be at least 6 characters, please try again!" + Reset);
                            System.out.print(Bold + "Enter password: " + Reset);
                            password = external_input.nextLine();
                            System.out.print(Bold + "Enter confirm password: " + Reset);
                            confirmPassword = external_input.nextLine();

                        } else {
                            System.out.println(Red + Bold + "Passwords do not match, please try again" + Reset);
                            System.out.print(Bold + "Enter password: " + Reset);

                            password = external_input.nextLine();
                            System.out.print("Enter confirm password: " + Reset);
                            confirmPassword = external_input.nextLine();
                        }
                    }

                    User newUser = new User(userName, mail, password);
                    Signup signup = new Signup(newUser);
                    subMenus(signup.login.getLoggedInUserFilename());
                    break;

                default:
                    System.out.println(Red + Bold + "\nInvalid option, please try again\n" + Reset);
                    break;
            }
            choice = Menu.displayMainMenuAuthentication();
        }
        external_input.close();
        System.out.print(Purple + Bold + "\nTHANKS FOR USING OUR APPLICATION!" + Reset);
        System.exit(0);
    }
}