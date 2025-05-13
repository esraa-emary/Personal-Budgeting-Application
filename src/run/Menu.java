package run;

import static authentication.Validation.*;
import static run.Format.*;

import Transactions.TransactionController;
import com.google.gson.Gson;
import dataStorage.userDatabase;
import income.Budget;
import income.Income;
import income.Expense;
import income.Goal;
import income.Reminder;
import payment.Debt;
import payment.Donate;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Iterator;

import dataStorage.*;

import java.util.List;


import com.google.gson.GsonBuilder;

import java.io.FileWriter;

/**
 * Provides the user interface and navigation system for the Personal Budgeting Application.
 * <p>
 * This class manages:
 * <ul>
 *   <li>Application menus and navigation between different sections</li>
 *   <li>User input validation for numbers, amounts, and dates</li>
 *   <li>Processing user selections for different application features</li>
 *   <li>Displaying formatted output for various financial data</li>
 * </ul>
 * <p>
 * The menu system is organized hierarchically with:
 * <ul>
 *   <li>Authentication menu (login/signup)</li>
 *   <li>Main sections menu (income/payment)</li>
 *   <li>Feature-specific submenus for each section</li>
 * </ul>
 *
 * @author Budget Application Team
 * @version 1.0
 * @see income.Budget
 * @see Transactions.TransactionController
 * @see authentication.Validation
 */

public class Menu {
    /**
     * Flag indicating whether the application should continue running
     */

    public static boolean isContinue = true;
    /**
     * Flag indicating whether to display the main menu
     */
    public static boolean isMain = true;
    /**
     * Scanner for reading user input throughout the application
     */
    public static Scanner input = new Scanner(System.in);

    /**
     * Validates user input as a valid numeric option.
     * <p>
     * Continues prompting until the user enters a valid number.
     *
     * @return The validated numeric input as an integer
     * @see authentication.Validation#isValidNumber(String)
     */
    public static int checkValidity() {
        String checkNumber = input.next();
        while (!isValidNumber(checkNumber)) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            System.out.printf(Bold + "choose an option: " + Reset);
            checkNumber = input.next();
        }
        return Integer.parseInt(checkNumber);
    }


    public static String getCurrentUserId(String filename) {
        try {
            File file = new File("files/users_db.json");
            if (!file.exists()) {
                System.out.println("User database not found");
                return "1"; // Default ID if not found
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            for (userEntry user : database.users) {
                if (user.filename.equals(filename) || user.current) {
                    return user.id;
                }
            }

            return "1"; // Default ID if not found
        } catch (Exception e) {
            System.out.println("Error getting user ID: " + e.getMessage());
            return "1"; // Default ID on error
        }
    }


    public static Budget manageBudgets(String userId) {

        try {
            File file = new File("files/users_db.json");
            if (!file.exists()) {
                System.out.println(Red + Bold + "User database not found" + Reset);
                return null;
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            userEntry user = null;
            for (userEntry u : database.users) {

                if (u.id.equals(userId)) {
                    user = u;
                    break;
                }
            }
            if (user == null) {
                System.out.println(Red + "User not found" + Reset);
                return null;
            }

            // Display budget management menu
            System.out.println(Bold + Cyan + "\n<------- Budget Management ------->\n" + Reset);
            System.out.println(Bold + "1 -> Switch Budget" + Reset);
            System.out.println(Bold + "2 -> Create New Budget" + Reset);
            System.out.println(Bold + "3 -> Delete Budget" + Reset);
            System.out.println(Bold + Red + "4 -> Back" + Reset);
            System.out.printf(Bold + "Choose an option: " + Reset);

            int choice = checkValidity();
            switch (choice) {
                case 1:
                    if (user.budgets == null || user.budgets.isEmpty()) {
                        System.out.println(Yellow + "No budgets available. Creating a new budget." + Reset);
                        return createNewBudget(userId);
                    } else {
                        return SelectBudget(userId, user.budgets);
                    }
                case 2:
                    return createNewBudget(userId);
                case 3:
                    deleteBudget(userId, user.budgets);
                    return null;
                case 4:
                default:
                    return null;
            }
        } catch (Exception e) {

            System.out.println(Red + "Error managing budgets: " + e.getMessage() + Reset);
            return null;
        }
    }

    private static Budget createNewBudget(String userId) {
        System.out.println(Bold + "Enter budget name" + Reset);
        String budgetName = input.nextLine();

        System.out.println(Bold + "Enter intial budget amount: " + Reset);
        double intitalAmount;
        try {
            intitalAmount = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(Red + "Invalid amount, using 0.0" + Reset);
            intitalAmount = 0.0;
        }

        Budget newBudget = new Budget(userId, budgetName, intitalAmount);
        newBudget.saveToJson();
        System.out.println(Bold + Green + "Budget created successfully!" + Reset);
        return newBudget;
    }

    private static Budget SelectBudget(String userId, List<Budget_data> budgets) {
        System.out.println(Bold + Cyan + "\n<------- Available Budgets ------->\n" + Reset);
        for (int i = 0; i < budgets.size(); i++) {
            System.out.println(Bold + (i + 1) + " -> " + budgets.get(i).getBudgetName() +
                    " ($" + budgets.get(i).getBudget() + ")" + Reset);
        }
        System.out.printf(Bold + "Choose a budget: " + Reset);

        int selection;
        try {
            selection = checkValidity();
            if (selection < 1 || selection > budgets.size()) {
                System.out.println(Red + "Invalid selection" + Reset);
                return null;
            }
        } catch (Exception e) {
            System.out.println(Red + "Invalid input" + Reset);
            return null;
        }

        return Budget.loadBudget(userId, budgets.get(selection - 1).getBudgetId());
    }

    private static void deleteBudget(String userId, List<Budget_data> budgets) {
        if (budgets == null || budgets.isEmpty()) {
            System.out.println(Yellow + "No budgets available to delete." + Reset);
            return;
        }

        if (budgets.size() == 1) {
            System.out.println(Red + "Cannot delete your only budget." + Reset);
            return;
        }

        // Show available budgets for deletion
        System.out.println(Bold + Cyan + "\n<------- Select Budget to Delete ------->\n" + Reset);
        for (int i = 0; i < budgets.size(); i++) {
            System.out.println(Bold + (i + 1) + " -> " + budgets.get(i).getBudgetName() + Reset);
        }
        System.out.printf(Bold + "Choose a budget to delete (or 0 to cancel): " + Reset);

        int selection = checkValidity();
        if (selection == 0) {
            System.out.println(Yellow + "Deletion cancelled." + Reset);
            return;
        }

        if (selection < 1 || selection > budgets.size()) {
            System.out.println(Red + "Invalid selection" + Reset);
            return;
        }

        try {
            // Remove the budget from the user's profile
            String budgetIdToDelete = budgets.get(selection - 1).getBudgetId();
            String budgetNameToDelete = budgets.get(selection - 1).getBudgetName();

            // Load the database
            File file = new File("files/users_db.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            // Find the user and remove the budget
            for (userEntry u : database.users) {
                if (u.id.equals(userId)) {
                    u.budgets.remove(selection - 1);
                    break;
                }
            }

            // Save the updated database
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(database));
            writer.close();

            System.out.println(Bold + Green + "Budget '" + budgetNameToDelete + "' deleted successfully!" + Reset);
        } catch (Exception e) {
            System.out.println(Red + "Error deleting budget: " + e.getMessage() + Reset);
        }
    }

    /**
     * Validates user input as a valid monetary amount.
     * <p>
     * Continues prompting until the user enters a valid number.
     *
     * @return The validated amount as an integer
     * @see authentication.Validation#isValidAmount(String)
     */

    public static int checkAmount() {
        String amount = input.next();
        while (!isValidAmount(amount)) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            amount = input.next();
        }
        return Integer.parseInt(amount);
    }

    /**
     * Validates user input as a valid date in YYYY-MM-DD format.
     * <p>
     * Continues prompting until the user enters a valid date.
     *
     * @return The validated date as a string in YYYY-MM-DD format
     * @see authentication.Validation#isValidDate(String)
     */

    public static String checkDate() {
        String date = input.next();
        while (!isValidDate(date)) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            date = input.next();
        }
        return date;
    }

    /**
     * Displays the authentication menu with login and signup options.
     * <p>
     * This is the entry point menu when the application starts.
     *
     * @return The user's selected option (1-3)
     */

    public static int displayMainMenuAuthentication() {
        System.out.println(Bold + Cyan + "<------- Welcome To our Personal Budgeting Application ------->\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Log-In." + Reset);
        System.out.println(Bold + "2 -> Sign-Up." + Reset);
        System.out.println(Bold + Red + "3 -> Exit." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Displays the main section selection menu after authentication.
     * <p>
     * Allows users to choose between Income and Payment sections,
     * or return to the authentication menu.
     *
     * @return The user's selected option (1-3)
     */

    public static int displayMainMenuSections() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To our Personal Budgeting Application\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Income Section." + Reset);
        System.out.println(Bold + "2 -> Payment Section." + Reset);
        System.out.println(Bold + Red + "3 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Displays the Income section menu with all available options.
     * <p>
     * Provides options for adding and viewing different income-related
     * data such as budget, expenses, income, goals, and reminders.
     *
     * @return The user's selected option (1-12)
     */

    public static int displayMainMenuIncome() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To Income Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Add Budget." + Reset);
        System.out.println(Bold + "2 -> Add Expense." + Reset);
        System.out.println(Bold + "3 -> Add Income." + Reset);
        System.out.println(Bold + "4 -> Add Goal." + Reset);
        System.out.println(Bold + "5 -> Add Reminder." + Reset);
        System.out.println(Bold + "6 -> Manage Budgets" + Reset);

        System.out.println(Bold + "7 -> Display Budgets." + Reset);
        System.out.println(Bold + "8 -> Display Expenses." + Reset);
        System.out.println(Bold + "9 -> Display Incomes." + Reset);
        System.out.println(Bold + "10 -> Display Goals." + Reset);
        System.out.println(Bold + "11 -> Display Reminders." + Reset);

        System.out.println(Bold + "12 -> Send Reminder." + Reset);

        System.out.println(Bold + Red + "13 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Displays the Payment section menu with all available options.
     * <p>
     * Provides options for managing transactions, debt repayments,
     * donations, and viewing financial reports.
     *
     * @return The user's selected option (1-7)
     */

    public static int displayMainMenuPayment() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To Payment Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Transactions Department." + Reset);
        System.out.println(Bold + "2 -> Add Debt Repayment." + Reset);
        System.out.println(Bold + "3 -> Add Donate." + Reset);
        System.out.println(Bold + "4 -> Display Debts Repayment." + Reset);
        System.out.println(Bold + "5 -> Display Donates." + Reset);
        System.out.println(Bold + "6 -> Financial Reports." + Reset);
        System.out.println(Bold + Red + "7 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Processes user selections within the Income section.
     * <p>
     * Based on the user's choice, this method:
     * <ul>
     *   <li>Collects necessary input for the selected operation</li>
     *   <li>Performs the requested operation on the budget object</li>
     *   <li>Saves any changes to the data file</li>
     *   <li>Displays results or status messages</li>
     * </ul>
     *
     * @param innerChoice    The user's selected option from the Income menu
     * @param bt             The budget object to operate on
     * @param fileName       The file name for persisting budget data
     * @param external_input Scanner for reading additional input
     */

    public static void optionsIncome(int innerChoice, Budget bt, String fileName, Scanner external_input) {
        String source, date, title;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                boolean case1_valid = false;
                while (!case1_valid) {
                    try {
                        System.out.print(Bold + "Please enter your" + Green + " budget" + Reset + Bold + " amount: " + Reset);
                        double bud_amount2;
                        do {
                            bud_amount2 = checkAmount();
                        } while (bud_amount2 <= 0);
                        bt.setBudget(bud_amount2);
                        bt.saveData(fileName);
                        case1_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }
                break;

            case 2:
                double am = 0;
                boolean case2_valid = false;
                while (!case2_valid) {
                    try {
                        System.out.print("How much did you pay: ");
                        do {
                            am = checkAmount();
                        } while (am <= 0);
                        case2_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }
                System.out.print("What did you exactly pay for: ");
                String cat = external_input.next();
                bt.addExpense(am, cat);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.print("Enter Source of income: ");
                source = external_input.next();
                System.out.print("How much is the amount of income: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addIncome(amount, source);
                bt.saveData(fileName);

                Iterator<Goal> iterator = bt.goals.iterator();
                while (iterator.hasNext()) {
                    Goal g = iterator.next();
                    if (bt.totalIncome - bt.totalExpense >= g.amount) {
                        System.out.println(Bold + Red + "You got your goal: " + g.amount + "$" + Reset);
                        iterator.remove();
                    }
                }
                break;

            case 4:
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = checkDate();
                System.out.print("How much is the amount of money is your goal by then: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addGoal(amount, date);
                bt.saveData(fileName);
                break;

            case 5:
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = checkDate();
                System.out.print("What is the title of this reminder: ");
                title = external_input.next();

                bt.addReminder(title, date);
                bt.saveData(fileName);
                break;


            case 6:
                String userId = getCurrentUserId(fileName);
                Budget newBudget = manageBudgets(userId);
                if (newBudget != null) {
                    bt = newBudget;
                    System.out.println(Bold + Green + "Budget switched successfully!" + Reset);
                }
                break;

            case 7:
                System.out.println("Your still have from you budget: " + Bold + Green + (bt.getBudget() - bt.totalExpense - bt.totalDonates - bt.totalDebts) + "$" + Reset);
                System.out.println("For expenses: " + Bold + Green + (bt.totalExpense) + "$" + Reset);
                System.out.println("For donates: " + Bold + Green + (bt.totalDonates) + "$" + Reset);
                System.out.println("For debts: " + Bold + Green + (bt.totalDebts) + "$" + Reset);
                System.out.println("You still have from you income: " + Bold + Green + (bt.totalIncome - bt.totalExpense) + "$" + Reset);
                break;

            case 8:
                System.out.println("Your" + Bold + Red + " Expenses" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Expense e : bt.expenses) {
                    System.out.println(Bold + counter + "- " + Red + e.amount + "$" + Reset + " for " + Bold + Blue + e.category + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalExpense + Reset + "\n");
                break;

            case 9:
                System.out.println("Your" + Bold + Red + " Income" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Income i : bt.incomes) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalIncome + Reset + "\n");
                break;

            case 10:
                System.out.println("Your" + Bold + Red + " Goals" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Goal g : bt.goals) {
                    System.out.println(Bold + counter + "- get " + Red + g.amount + "$" + Reset + " by " + Bold + Blue + g.date + Reset);
                    counter++;
                }
                break;

            case 11:
                System.out.println("Your" + Bold + Red + " Reminders" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Reminder r : bt.reminders) {
                    System.out.println(Bold + counter + "- reminder about " + Red + r.title + Reset + " on " + Bold + Blue + r.date + Reset);
                    counter++;
                }
                break;

            case 12:
                bt.sendReminder(bt, external_input);
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 8.");
                break;
        }
    }

    /**
     * Processes user selections within the Payment section.
     * <p>
     * Based on the user's choice, this method:
     * <ul>
     *   <li>Navigates to transaction management if selected</li>
     *   <li>Collects necessary input for debt repayments or donations</li>
     *   <li>Displays financial reports and transaction histories</li>
     *   <li>Saves any changes to the data file</li>
     * </ul>
     *
     * @param innerChoice    The user's selected option from the Payment menu
     * @param bt             The budget object to operate on
     * @param fileName       The file name for persisting budget data
     * @param external_input Scanner for reading additional input
     * @param tc             The transaction controller for recurring transaction management
     */

    public static void optionsPayment(int innerChoice, Budget bt, String fileName, Scanner external_input, TransactionController tc) {
        String source;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                tc.showRecurringTransactionMenu();
                break;

            case 2:
                System.out.print("Enter source of debt: ");
                source = external_input.next();
                System.out.print("How much is the amount of debt: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addDebt(amount, source);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.print("Enter source to donate: ");
                source = external_input.next();
                System.out.print("How much is the amount of money: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addDonate(amount, source);
                bt.saveData(fileName);
                break;

            case 4:
                System.out.println("Your" + Bold + Red + " Debts" + Reset + Bold + " are:" + Reset);
                counter = 1;
                for (Debt r : bt.debts) {
                    System.out.println(Bold + counter + "- debt for " + Red + r.source + Reset + " with " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }
                break;

            case 5:
                System.out.println("Your" + Bold + Red + " Donates" + Reset + Bold + " are:" + Reset);
                counter = 1;
                for (Donate r : bt.donates) {
                    System.out.println(Bold + counter + "- donate to " + Red + r.source + Reset + " by " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }
                break;

            case 6:
                // income
                System.out.println("Your total income is: " + Bold + Green + (bt.totalIncome) + "$\n" + Reset + "Which comes from:");
                counter = 1;
                for (Income i : bt.incomes) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }

                // budget
                System.out.println("Your budget is: " + Bold + Green + (bt.budget) + "$\n" + Reset);

                // transaction
                System.out.println("Your transactions come from:");
                counter = 1;
//                for (Transaction t : tc.) {
//                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
//                    counter++;
//                }
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 6.");
        }
    }
}
