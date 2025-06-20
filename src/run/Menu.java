package run;

import static authentication.Validation.*;
import static income.Budget.getCurrentUserId;
import static income.Budget.manageBudgets;
import static run.Format.*;

import Transactions.Transaction;
import Transactions.TransactionController;
import Transactions.TransactionService;
import authentication.Validation;
import income.Budget;
import income.Income;
import income.Expense;
import income.Goal;
import income.Reminder;
import payment.Debt;
import payment.Donate;

import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

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
 * @see Budget
 * @see TransactionController
 * @see Validation
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
     * @see Validation#isValidNumber(String)
     */
    public static int checkValidity() {
        String checkNumber = input.nextLine();
        while (!isValidNumber(checkNumber) || checkNumber.isEmpty()) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            System.out.printf(Bold + "choose an option: " + Reset);
            checkNumber = input.nextLine();
        }
        return Integer.parseInt(checkNumber);
    }


    /**
     * Validates user input as a valid monetary amount.
     * <p>
     * Continues prompting until the user enters a valid number.
     *
     * @return The validated amount as an integer
     * @see Validation#isValidAmount(String)
     */

    public static int checkAmount() {
        String amount = input.nextLine();
        while (!isValidAmount(amount) || amount.isEmpty()) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            amount = input.nextLine();
        }
        return Integer.parseInt(amount);
    }

    /**
     * Validates user input as a valid date in YYYY-MM-DD format.
     * <p>
     * Continues prompting until the user enters a valid date.
     *
     * @return The validated date as a string in YYYY-MM-DD format
     * @see Validation#isValidDate(String)
     */

    public static String checkDate() {
        String date = input.nextLine();
        while (!isValidDate(date) || date.isEmpty()) {
            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
            date = input.nextLine();
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
        System.out.println(Bold + "1 -> Edit Budget" + Reset);
        System.out.println(Bold + "2 -> Add Expense" + Reset);
        System.out.println(Bold + "3 -> Add Income" + Reset);
        System.out.println(Bold + "4 -> Add Goal" + Reset);
        System.out.println(Bold + "5 -> Add Reminder" + Reset);
        System.out.println(Bold + "6 -> Manage Budgets" + Reset);

        System.out.println(Bold + "7 -> Display Chosen Budget" + Reset);
        System.out.println(Bold + "8 -> Display Expenses" + Reset);
        System.out.println(Bold + "9 -> Display Incomes" + Reset);
        System.out.println(Bold + "10 -> Display Goals" + Reset);
        System.out.println(Bold + "11 -> Display Reminders" + Reset);

        System.out.println(Bold + "12 -> Send Reminder" + Reset);

        System.out.println(Bold + Red + "13 -> Back" + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Displays the Payment section menu with all available options.
     * <p>
     * Provides options for managing transactions, debt repayments,
     * donations, and viewing financial reports. Users can also
     * return to the previous menu.
     * <p>
     * The menu includes:
     * <ul>
     *   <li>Transaction management functionality</li>
     *   <li>Debt repayment tracking</li>
     *   <li>Donation management</li>
     *   <li>Financial reporting</li>
     * </ul>
     *
     * @return The user's selected option (1-7)
     * @see Budget#addDebt(double, String)
     * @see Budget#addDonate(double, String)
     * @see TransactionController
     */

    public static int displayMainMenuPayment() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To Payment Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Transactions Department" + Reset);
        System.out.println(Bold + "2 -> Add Debt Repayment" + Reset);
        System.out.println(Bold + "3 -> Add Donate" + Reset);
        System.out.println(Bold + "4 -> Display Debts Repayment" + Reset);
        System.out.println(Bold + "5 -> Display Donates" + Reset);
        System.out.println(Bold + "6 -> Financial Reports" + Reset);
        System.out.println(Bold + Red + "7 -> Back" + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        return checkValidity();
    }

    /**
     * Processes user selections within the Income section.
     * <p>
     * Based on the user's choice, this method:
     * <ul>
     *   <li>Handles adding budgets, expenses, incomes, goals, and reminders</li>
     *   <li>Manages switching budgets</li>
     *   <li>Displays financial data such as budgets, expenses, incomes, goals, and reminders</li>
     *   <li>Sends reminders</li>
     * </ul>
     * <p>
     * This method validates user input and performs the requested operation
     * on the provided budget object, saving any changes to the data file.
     *
     * @param innerChoice    The user's selected option from the Income menu
     * @param bt             The budget object to operate on
     * @param fileName       The file name for persisting budget data
     * @param external_input Scanner for reading additional input
     */

    public static Budget optionsIncome(int innerChoice, Budget bt, String fileName, Scanner external_input) {
        String source = "", date, title;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                boolean case1_valid = false;
                while (!case1_valid) {
                    try {
                        System.out.print(Bold + "Please enter new" + Green + " budget" + Reset + Bold + " amount for '" + bt.budgetName + "': " + Reset);
                        double bud_amount2;
                        do {
                            bud_amount2 = checkAmount();
                        } while (bud_amount2 <= 0);

                        // Update the budget amount
                        bt.setBudget(bud_amount2);

                        // Save using the new JSON-based method instead of the old file method
                        bt.saveToJson();

                        System.out.println(Bold + Green + "Budget amount updated successfully!" + Reset);
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
                        System.out.print(Bold + "How much did you pay for '" + bt.budgetName + "': " + Reset);
                        do {
                            am = checkAmount();
                        } while (am <= 0);
                        case2_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }
                System.out.print(Bold + "What did you exactly pay for: " + Reset);
                String cat = external_input.nextLine();
                while (cat.isEmpty()) {
                    System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
                    cat = external_input.nextLine();
                }

                bt.addExpense(am, cat);
                bt.saveToJson(); // Using the new JSON saving method instead of file-based approach
                break;
            case 3:
                boolean case3_valid = false;
                double incomeAmount = 0;
                while (!case3_valid) {
                    try {
                        System.out.print(Bold + "Enter Source of income: " + Reset);
                        source = external_input.nextLine();
                        while (source.isEmpty()) {
                            System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
                            source = external_input.nextLine();
                        }

                        System.out.print(Bold + "How much is the amount of income: " + Reset);
                        do {
                            incomeAmount = checkAmount();
                        } while (incomeAmount <= 0);
                        case3_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }

                // Add income to the user's general income list in the JSON file
                // The method already handles JSON file saving and goal checking internally
                bt.addIncome(incomeAmount, source);

                break;
            case 4:
                System.out.println(Bold + "Adding goal to budget: " + Cyan + bt.budgetName + Reset);
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = checkDate();
                System.out.print("How much is the amount of money is your goal by then: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addGoal(amount, date);
                bt.saveToJson(); // Use JSON saving instead of file-based approach
                break;

            case 5:
                System.out.println(Bold + "Adding reminder to budget: " + Cyan + bt.budgetName + Reset);
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = checkDate();
                System.out.print("What is the title of this reminder: ");
                title = external_input.nextLine();
                while (title.isEmpty()) {
                    System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
                    title = external_input.nextLine();
                }

                bt.addReminder(title, date);
                bt.saveToJson(); // Use JSON saving instead of file-based approach
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

                System.out.println("Your still have from you budget: " + Bold + Green + (bt.getBudget() - bt.totalExpense) + "$" + Reset);
                System.out.println("For expenses: " + Bold + Green + (bt.totalExpense) + "$" + Reset);
                System.out.println("For donates: " + Bold + Green + (bt.totalDonates) + "$" + Reset);
                System.out.println("For debts: " + Bold + Green + (bt.totalDebts) + "$" + Reset);
                System.out.println("You still have from you income: " + Bold + Green + (bt.totalIncome - bt.getBudget() - bt.totalDonates - bt.totalDebts) + "$" + Reset);
                break;

            case 8:
                System.out.println(Bold + Cyan + "Budget: " + bt.budgetName + Reset);
                System.out.println("Your" + Bold + Red + " Expenses" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Expense e : bt.expenses) {
                    System.out.println(Bold + counter + "- " + Red + e.amount + "$" + Reset + " for " + Bold + Blue + e.category + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalExpense + "$" + Reset + "\n");
                break;

            case 9:
                System.out.println("Your" + Bold + Red + " Income" + Reset + Bold + " (across all budgets) are:" + Reset);

                counter = 1;
                // Get incomes from JSON file using the getIncomes method
                List<Income> incomes = bt.getIncomes();
                for (Income i : bt.getIncomes()) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalIncome + "$" + Reset + "\n");
                break;

            case 10:
                System.out.println(Bold + Cyan + "Budget: " + bt.budgetName + Reset);
                System.out.println("Your" + Bold + Red + " Goals" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Goal g : bt.goals) {
                    System.out.println(Bold + counter + "- get " + Red + g.amount + "$" + Reset + " by " + Bold + Blue + g.date + Reset);
                    counter++;
                }
                break;

            case 11:
                System.out.println(Bold + Cyan + "Budget: " + bt.budgetName + Reset);
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
                System.out.println(Red + Bold + "Invalid option. Please choose a number between 1 and 13." + Reset);
                break;
        }

        return bt;
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

    public static void optionsPayment(int innerChoice, Budget bt, String fileName, Scanner external_input, TransactionController tc, TransactionService ts) {
        String source;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                tc.showRecurringTransactionMenu();
                break;

            case 2:
                System.out.print("Enter source of debt: ");
                source = external_input.nextLine();
                while (source.isEmpty()) {
                    System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
                    source = external_input.nextLine();
                }

                System.out.print("How much is the amount of debt: ");
                do {
                    amount = checkAmount();
                } while (amount <= 0);

                bt.addDebt(amount, source);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.print("Enter source to donate: ");
                source = external_input.nextLine();
                while (source.isEmpty()) {
                    System.out.println(Red + Bold + "\nInvalid input, please try again" + Reset);
                    source = external_input.nextLine();
                }

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

                List<Income> incomes = bt.getIncomes();
                counter = 1;
                for (Income i : incomes) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }

                // debts
                System.out.println("\nYour" + Bold + Red + " Debts" + Reset + Bold + " are:" + Reset);
                counter = 1;
                for (Debt r : bt.debts) {
                    System.out.println(Bold + counter + "- debt for " + Red + r.source + Reset + " with " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }

                // donates
                System.out.println("\nYour" + Bold + Red + " Donates" + Reset + Bold + " are:" + Reset);
                counter = 1;
                for (Donate r : bt.donates) {
                    System.out.println(Bold + counter + "- donate to " + Red + r.source + Reset + " by " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }

                // budget
                System.out.println("\nYour current budget is: " + Bold + Green + (bt.budget) + "$\n" + Reset);

                // transaction
                System.out.println("Your transactions from this budget come from:");
                counter = 1;
                for (Transaction t : ts.recurringTransactions) {
                    System.out.printf("ID: %d | %s | Amount: %.2f | Category: %s | Next: %s%n",
                            t.getId(),
                            t.getDescription(),
                            t.getAmount(),
                            t.getCategory(),
                            t.getNextOccurrence());
                    counter++;
                }
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 7.");
        }
    }
}
