package income;

import static run.Format.*;
import static run.Menu.checkValidity;
import static run.Menu.input;

import java.io.*;
import java.util.*;

import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;
import dataStorage.*;
import payment.Debt;
import payment.Donate;

/**
 * Manages a user's personal budget, expenses, incomes, and financial goals.
 * <p>
 * This class provides comprehensive budget management functionality including:
 * <ul>
 *   <li>Budget setting and tracking</li>
 *   <li>Expense management with category tracking</li>
 *   <li>Income tracking with source information</li>
 *   <li>Financial goal setting with target dates</li>
 *   <li>Reminders with email notification capability</li>
 *   <li>Donation tracking</li>
 *   <li>Debt tracking</li>
 *   <li>Data persistence through file storage</li>
 * </ul>
 * <p>
 * Budget constraints are enforced when adding new expenses, ensuring users
 * cannot exceed their set budget limits.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see income.Expense
 * @see income.Income
 * @see income.Goal
 * @see income.Reminder
 * @see payment.Debt
 * @see payment.Donate
 */
public class Budget {
    /**
     * The total budget amount set by the user
     */
    public double budget;

    /**
     * Collection of user's expenses with categories
     */
    public ArrayList<Expense> expenses = new ArrayList<>();

    /**
     * Sum of all expenses
     */
    public double totalExpense = 0;

    /**
     * Collection of user's income sources
     */
    public ArrayList<Income> incomes = new ArrayList<>();


    /**
     * Collection of user's financial goals
     */
    public ArrayList<Goal> goals = new ArrayList<>();

    /**
     * Collection of user's reminders
     */
    public ArrayList<Reminder> reminders = new ArrayList<>();

    /**
     * Collection of user's donations
     */
    public ArrayList<Donate> donates = new ArrayList<>();

    /**
     * Sum of all donations
     */
    public double totalDonates = 0;

    /**
     * Collection of user's debts
     */
    public ArrayList<Debt> debts = new ArrayList<>();

    /**
     * Sum of all debts
     */
    public double totalDebts = 0;

    /**
     * Path to the JSON file storing user data
     */
    private static final String USERS_DB_FILE_PATH = "files/users_db.json";

    /**
     * Email address for reminder notifications
     */
    private String mailReminder = "";

    /**
     * Username for reminder notifications
     */
    private String usernameReminder = "";

    private String userId;
    private String budgetId;
    public String budgetName;
    public double totalIncome;

    /**
     * Creates a new budget with the specified initial amount.
     *
     * @param budget The starting budget amount
     */
    public Budget(double budget) {
        this.budget = budget;
    }

    /**
     * Creates a budget and loads existing data from the specified file.
     *
     * @see #loadData(String)
     */
    public Budget(String userId, String budgetName, double initialAmount) {

        this.userId = userId;
        this.budgetId = budgetName;
        this.budgetName = budgetName;
        this.budget = initialAmount;
        this.budgetId = "budget" + UUID.randomUUID().toString().substring(0, 8);
        this.totalIncome = calculateUserTotalIncome();
    }

    /**
     * Loads a budget for a specific user based on the provided user ID and budget ID.
     * <p>
     * This method reads the user database from a JSON file and retrieves the budget
     * associated with the given user ID and budget ID. If the user or budget is not found,
     * appropriate error messages are displayed, and {@code null} is returned.
     * </p>
     *
     * @param userId   The ID of the user whose budget is being loaded.
     * @param budgetId The ID of the budget to load.
     * @return The {@code Budget} object if found, or {@code null} if the user or budget is not found.
     */

    public static Budget loadBudget(String userId, String budgetId) {

        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                System.out.println("User database not found");
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

                System.out.println("User not found");
                return null;
            }

            Budget_data budgetData = null;
            for (Budget_data bd : user.budgets) {
                if (bd.getBudgetId().equals(budgetId)) {
                    budgetData = bd;
                    break;
                }
            }

            if (budgetData == null) {
                System.out.println("Budget not found");
                return null;
            }

            Budget budget = new Budget(budgetData.getBudget());
            budget.userId = userId;
            budget.budgetId = budgetId;
            budget.budgetName = budgetData.getBudgetName();
            budget.totalExpense = budgetData.getTotalExpenses();
            budget.totalDonates = budgetData.getTotalDonates();
            budget.totalDebts = budgetData.getTotalDebts();
            budget.expenses = new ArrayList<>(budgetData.getExpenses());
            budget.goals = new ArrayList<>(budgetData.getGoals());
            budget.reminders = new ArrayList<>(budgetData.getReminders());
            budget.donates = new ArrayList<>(budgetData.getDonates());
            budget.debts = new ArrayList<>(budgetData.getDebts());

            budget.totalIncome = budget.calculateUserTotalIncome();

            return budget;
        } catch (Exception e) {
            System.out.println("Error loading budget: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves the current budget data to the JSON database.
     * <p>
     * This method updates the user's budget information in the JSON file located
     * at the specified path. If the budget already exists, it is updated; otherwise,
     * a new budget entry is added. The method ensures that the `userId` and `budgetId`
     * are not null before proceeding.
     * </p>
     * <p>
     * If the user or database is not found, appropriate error messages are displayed.
     * </p>
     *
     * @throws IOException If an error occurs while reading or writing the JSON file.
     */

    public void saveToJson() {

        if (userId == null || budgetId == null) {
            System.out.println("Cannot save: Budget ID or User ID is missing");
            return;
        }

        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                System.out.println("User database not found");
                return;
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
                System.out.println("User not found");
                return;

            }

            boolean budgetFound = false;
            for (int i = 0; i < user.budgets.size(); i++) {
                if (user.budgets.get(i).getBudgetId().equals(budgetId)) {
                    Budget_data update_budget = new Budget_data(this, budgetId, budgetName);
                    user.budgets.set(i, update_budget);
                    budgetFound = true;
                    break;
                }
            }

            if (!budgetFound) {
                Budget_data new_budget_data = new Budget_data(this, budgetId, budgetName);
                user.budgets.add(new_budget_data);
            }

            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(database));
            writer.close();

            System.out.println("Budget saved successfully!");

        } catch (Exception e) {
            System.out.println("Error saving budget: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculates the total income for the current user.
     * <p>
     * This method reads the user database from a JSON file and sums up all income
     * entries associated with the current user's ID. If the user or their incomes
     * are not found, it returns 0.0.
     * </p>
     * <p>
     * If an error occurs during file reading or parsing, it logs the error and
     * returns 0.0.
     * </p>
     *
     * @return The total income for the current user as a double, or 0.0 if no income is found or an error occurs.
     */

    private double calculateUserTotalIncome() {
        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                return 0.0;
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            for (userEntry u : database.users) {
                if (u.id.equals(userId)) {
                    double total = 0.0;
                    if (u.incomes != null) {
                        for (Income income : u.incomes) {
                            total += income.amount;
                        }
                    }
                    return total;
                }
            }
            return 0.0;
        } catch (Exception e) {
            System.out.println("Error calculating user total income: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Retrieves the current user's ID based on the provided filename or the current user flag.
     * <p>
     * This method reads the user database from a JSON file and searches for a user whose
     * `filename` matches the provided filename or is marked as the current user. If no match
     * is found, it returns a default ID of "1".
     * </p>
     * <p>
     * If the user database file does not exist or an error occurs during processing, the method
     * logs an error message and returns the default ID of "1".
     * </p>
     *
     * @param filename The filename associated with the user to search for.
     * @return The user ID as a {@code String}, or "1" if no user is found or an error occurs.
     */

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

    /**
     * Manages the budgets for a specific user.
     * <p>
     * This method provides a menu for managing budgets, allowing the user to:
     * <ul>
     *   <li>Switch to an existing budget</li>
     *   <li>Create a new budget</li>
     *   <li>Delete an existing budget</li>
     *   <li>Exit the budget management menu</li>
     * </ul>
     * The method reads the user database from a JSON file, identifies the user by their ID,
     * and performs the selected operation. If the user or database is not found, appropriate
     * error messages are displayed.
     * </p>
     *
     * @param userId The ID of the user whose budgets are being managed
     * @return The selected or newly created {@code Budget} object, or {@code null} if no budget is selected
     */

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
            System.out.println(Bold + "4 -> Display All Budgets" + Reset);
            System.out.println(Bold + Red + "5 -> Back" + Reset);
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
                    displayAllBudgets(userId, user.budgets);
                    return manageBudgets(userId); // Return to budget management menu after displaying
                case 5:
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println(Red + "Error managing budgets: " + e.getMessage() + Reset);
            return null;
        }
    }

    /**
     * Displays detailed information about all budgets belonging to a user.
     *
     * @param userId  The ID of the user whose budgets are being displayed
     * @param budgets The list of budget data objects for the user
     */
    private static void displayAllBudgets(String userId, List<Budget_data> budgets) {
        if (budgets == null || budgets.isEmpty()) {
            System.out.println(Yellow + "No budgets available to display." + Reset);
            return;
        }

        System.out.println(Bold + Cyan + "\n<------- All Budgets Summary ------->\n" + Reset);

        int counter = 1;
        for (Budget_data budgetData : budgets) {
            // Load the full budget to get all details
            Budget budget = loadBudget(userId, budgetData.getBudgetId());

            if (budget != null) {
                System.out.println(Bold + "\n" + counter + ". Budget: " + Green + budget.budgetName + Reset);
                System.out.println("   Budget Amount: " + Bold + Green + budget.getBudget() + "$" + Reset);
                System.out.println("   Remaining Budget: " + Bold + Green + (budget.getBudget() - budget.totalExpense - budget.totalDonates - budget.totalDebts) + "$" + Reset);
                System.out.println("   Total Expenses: " + Bold + Red + budget.totalExpense + "$" + Reset);
                System.out.println("   Total Income: " + Bold + Green + budget.totalIncome + "$" + Reset);
                System.out.println("   Total Donations: " + Bold + Blue + budget.totalDonates + "$" + Reset);
                System.out.println("   Total Debts: " + Bold + Purple + budget.totalDebts + "$" + Reset);

                // Display expenses
                if (!budget.expenses.isEmpty()) {
                    System.out.println("   " + Bold + "Expenses:" + Reset);
                    int expCounter = 1;
                    for (Expense e : budget.expenses) {
                        System.out.println("     " + expCounter + ". " + Red + e.amount + "$" + Reset + " for " + Blue + e.category + Reset);
                        expCounter++;
                    }
                }

                // Display goals
                if (!budget.goals.isEmpty()) {
                    System.out.println("   " + Bold + "Goals:" + Reset);
                    int goalCounter = 1;
                    for (Goal g : budget.goals) {
                        System.out.println("     " + goalCounter + ". Get " + Green + g.amount + "$" + Reset + " by " + Blue + g.date + Reset);
                        goalCounter++;
                    }
                }

                // Display reminders
                if (!budget.reminders.isEmpty()) {
                    System.out.println("   " + Bold + "Reminders:" + Reset);
                    int remCounter = 1;
                    for (Reminder r : budget.reminders) {
                        System.out.println("     " + remCounter + ". " + Yellow + r.title + Reset + " on " + Blue + r.date + Reset);
                        remCounter++;
                    }
                }

                counter++;
            }
        }
    }

    /**
     * Creates a new budget for the specified user.
     * <p>
     * This method prompts the user to enter a budget name and an initial budget amount.
     * If the entered amount is invalid, it defaults to 0.0. The new budget is then saved
     * to the JSON database and returned.
     * </p>
     *
     * @param userId The ID of the user for whom the budget is being created.
     * @return The newly created {@code Budget} object.
     */

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

    /**
     * Allows the user to select a budget from a list of available budgets.
     * <p>
     * This method displays all available budgets for the specified user and prompts
     * the user to select one. If the selection is invalid, an error message is displayed,
     * and the method returns {@code null}. If a valid selection is made, the corresponding
     * budget is loaded and returned.
     * </p>
     *
     * @param userId  The ID of the user whose budgets are being displayed.
     * @param budgets A list of {@code Budget_data} objects representing the user's budgets.
     * @return The selected {@code Budget} object, or {@code null} if the selection is invalid.
     */

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


    /**
     * Deletes a budget from the user's list of budgets.
     * <p>
     * This method allows the user to select a budget to delete from their list.
     * It ensures that at least one budget remains and updates the user database
     * after deletion. If the user cancels or makes an invalid selection, the
     * operation is aborted.
     * </p>
     *
     * @param userId  The ID of the user whose budget is being deleted
     * @param budgets The list of budgets associated with the user
     */

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

    public double getBudget() {

        return this.budget;
    }

    /**
     * Sets the budget to a new amount.
     *
     * @param budget The new budget amount
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * Creates a default budget for a user with no existing budgets.
     *
     * @param userId The ID of the user
     * @return A new Budget object
     */


    private static Budget createDefaultBudget(String userId) {
        Budget budget = new Budget(userId, "Main Budget", 0.0);
        budget.saveToJson();
        System.out.println(Bold + Green + "Created new default budget" + Reset);
        return budget;
    }

    /**
     * Gets an existing budget for the user or creates a new one.
     *
     * @param userId The ID of the user
     * @return A Budget object
     */
    public static Budget getUserBudget(String userId) {
        try {
            File file = new File("files/users_db.json");
            if (!file.exists()) {
                System.out.println("User database not found");
                return createDefaultBudget(userId);
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            for (userEntry user : database.users) {
                if (user.id.equals(userId)) {
                    // Check if user has any budgets
                    if (user.budgets != null && !user.budgets.isEmpty()) {
                        // If multiple budgets, let user select one
                        if (user.budgets.size() > 1) {
                            return selectUserBudget(userId, user.budgets);
                        } else {
                            // Just use the only budget
                            return Budget.loadBudget(userId, user.budgets.get(0).getBudgetId());
                        }
                    } else {
                        // Create a default budget if none exists
                        return createDefaultBudget(userId);
                    }
                }
            }

            // User not found, create default budget
            return createDefaultBudget(userId);
        } catch (Exception e) {
            System.out.println("Error loading user budget: " + e.getMessage());
            return createDefaultBudget(userId);
        }
    }


    /**
     * Displays a menu to let the user select from multiple budgets.
     *
     * @param userId  The ID of the user
     * @param budgets The list of available budgets
     * @return The selected Budget object
     */
    public static Budget selectUserBudget(String userId, List<Budget_data> budgets) {
        Scanner input = new Scanner(System.in);

        System.out.println(Bold + Cyan + "\n<------- Your Budgets ------->\n" + Reset);
        for (int i = 0; i < budgets.size(); i++) {
            System.out.println(Bold + (i + 1) + " -> " + budgets.get(i).getBudgetName() +
                    " ($" + budgets.get(i).getBudget() + ")" + Reset);
        }
        System.out.println(Bold + (budgets.size() + 1) + " -> Create New Budget" + Reset);
        System.out.printf(Bold + "Choose a budget: " + Reset);

        int selection = -1;
        while (selection < 1 || selection > budgets.size() + 1) {
            try {
                selection = Integer.parseInt(input.nextLine());
                if (selection < 1 || selection > budgets.size() + 1) {
                    System.out.println(Red + Bold + "Invalid option. Please try again: " + Reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(Red + Bold + "Please enter a number: " + Reset);
            }
        }

        // Create new budget if selected
        if (selection == budgets.size() + 1) {
            System.out.print(Bold + "Enter budget name: " + Reset);
            String budgetName = input.nextLine();

            System.out.print(Bold + "Enter initial amount: " + Reset);
            double initialAmount = 0.0;
            try {
                initialAmount = Double.parseDouble(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(Red + "Invalid amount, using 0.0" + Reset);
            }

            Budget newBudget = new Budget(userId, budgetName, initialAmount);
            newBudget.saveToJson();
            return newBudget;
        } else {
            // Load the selected budget
            return Budget.loadBudget(userId, budgets.get(selection - 1).getBudgetId());
        }
    }


    /**
     * Adds a new expense if it doesn't exceed the budget.
     * <p>
     * If adding this expense would cause the total expenses to exceed
     * the budget, the addition is rejected with a warning message.
     *
     * @param amount   The expense amount
     * @param category The category of the expense
     */
    public void addExpense(double amount, String category) {
        if (totalExpense + amount > budget) {
            System.out.println("Exceeded budget, you cannot add this expense!");
        } else {
            expenses.add(new Expense(amount, category));
            totalExpense += amount;
            System.out.println("Expense added: " + "$" + Bold + Blue + amount + Reset + " for " + Bold + Blue + category + Reset);
            saveToJson();
        }
    }

    /**
     * Adds a new income source to the budget.
     *
     * @param amount The income amount
     * @param source The source of the income
     */

    public void addIncome(double amount, String source) {
        try {
            // Read the current database
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                System.out.println("User database not found");
                return;
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            // Find the user
            for (userEntry user : database.users) {
                if (user.id.equals(userId)) {
                    // Initialize incomes list if null
                    if (user.incomes == null) {
                        user.incomes = new ArrayList<>();
                    }

                    // Add the new income
                    user.incomes.add(new Income(amount, source));

                    // Save the updated database
                    FileWriter writer = new FileWriter(file);
                    writer.write(gson.toJson(database));
                    writer.close();

                    // Update local total income
                    this.totalIncome = calculateUserTotalIncome();

                    System.out.println("Income added: " + "$" + Bold + Blue + amount + Reset + " from " + Bold + Blue + source + Reset);

                    // Check goals after adding income
                    checkGoals();
                    return;
                }
            }
            System.out.println("User not found");
        } catch (Exception e) {
            System.out.println("Error adding income: " + e.getMessage());
        }
    }


    /**
     * Retrieves the list of incomes for the current user.
     * <p>
     * This method reads the user database file and fetches the list of incomes
     * associated with the current user's ID. If the user or their incomes are not
     * found, an empty list is returned.
     * </p>
     *
     * @return A list of {@code Income} objects for the current user, or an empty list if no incomes are found.
     */


    public List<Income> getIncomes() {
        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }


            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            for (userEntry u : database.users) {
                if (u.id.equals(userId)) {
                    return u.incomes != null ? u.incomes : new ArrayList<>();
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error getting incomes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new financial goal with target amount and date.
     *
     * @param amount The target amount for the goal
     * @param date   The target date to achieve the goal
     */
    public void addGoal(double amount, String date) {
        goals.add(new Goal(amount, date));
        System.out.println("Goal added: " + "$" + Bold + Blue + amount + Reset + " by " + Bold + Blue + date + Reset);
        saveToJson();
        checkGoals();
    }

    /**
     * Adds a new reminder with title and date.
     *
     * @param title The title/description of the reminder
     * @param date  The date for the reminder
     */
    public void addReminder(String title, String date) {
        reminders.add(new Reminder(title, date));
        System.out.println("Reminder added: " + "$" + Bold + Blue + title + Reset + " on " + Bold + Blue + date + Reset);
        saveToJson();
    }

    /**
     * Adds a new donation if it doesn't exceed the budget.
     * <p>
     * If adding this donation would cause the total donations to exceed
     * the budget, the addition is rejected with a warning message.
     *
     * @param amount The donation amount
     * @param source The recipient of the donation
     */
    public void addDonate(double amount, String source) {
        if (totalDonates + amount > totalIncome) {
            System.out.println("Exceeded budget, you cannot add this donate!");
        } else {
            donates.add(new Donate(amount, source));
            totalDonates += amount;
            System.out.println("Donate added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
            saveToJson();
        }
    }

    /**
     * Adds a new debt repayment if it doesn't exceed the budget.
     * <p>
     * If adding this debt would cause the total debts to exceed
     * the budget, the addition is rejected with a warning message.
     *
     * @param amount The debt repayment amount
     * @param source The creditor of the debt
     */
    public void addDebt(double amount, String source) {
        if (totalDebts + amount > totalIncome) {
            System.out.println("Exceeded budget, you cannot repayment this debt!");
        } else {
            debts.add(new Debt(amount, source));
            totalDebts += amount;
            System.out.println("Debt added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
            saveToJson();
        }
    }


    /**
     * Checks and updates the status of financial goals.
     * <p>
     * This method iterates through the list of financial goals and determines
     * if any goals have been achieved based on the difference between the total
     * income and total expenses. Achieved goals are removed from the list and
     * saved to persistent storage.
     * </p>
     * <p>
     * If any goals are achieved, the method saves the updated budget data to the
     * JSON file.
     * </p>
     */

    private void checkGoals() {
        Iterator<Goal> iterator = goals.iterator();
        List<Goal> achievedGoals = new ArrayList<>();

        while (iterator.hasNext()) {
            Goal goal = iterator.next();
            if (totalIncome - totalExpense >= goal.amount) {
                System.out.println(Bold + Green + "You achieved your goal: " + goal.amount + "$" + Reset);
                achievedGoals.add(goal);
                iterator.remove();
            }
        }

        if (!achievedGoals.isEmpty()) {
            saveToJson();
        }
    }

    /**
     * Sends an email reminder for a specific date.
     * <p>
     * This method:
     * <ol>
     *   <li>Retrieves the current user's email and username from the database</li>
     *   <li>Searches for reminders matching the specified date</li>
     *   <li>Uses the Mailjet API to send an email reminder</li>
     * </ol>
     *
     * @param bt             The budget object containing reminders
     * @param external_input Scanner for user input to specify the reminder date
     */
    public void sendReminder(Budget bt, Scanner external_input) {
        try {
            File file = new File(USERS_DB_FILE_PATH);
            if (!file.exists()) {
                System.out.println("No user database found, please register first");
                System.exit(0);
            }

            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            userDatabase database = gson.fromJson(reader, userDatabase.class);
            reader.close();

            if (database == null || database.users == null || database.users.isEmpty()) {
                System.out.println("User database is empty or invalid.");
                return;
            }

            for (userEntry user : database.users) {
                if (user.current) {
                    usernameReminder = user.username;
                    mailReminder = user.email;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Enter date to send reminder as (YYYY-MM-DD): ");
        String date = external_input.next();

        boolean found = false;
        for (int remind = 0; remind < bt.reminders.size(); remind++) {
            if (bt.reminders.get(remind).date.equals(date)) {
                found = true;

                String client_name = usernameReminder;
                String api_key_public = "998d404401aaaca06cea222204ee2179";
                String api_key_private = "bae1a8be1b381bf1b52f31ea2e207d30";
                String to_email = mailReminder;
                String from_email = "assignmentsoftware16@gmail.com";
                String subject = "Reminder for " + bt.reminders.get(remind).title + " on " + date;
                String message_body = "Dear " + client_name + ", Your reminder is on " + date +
                        " for " + bt.reminders.get(remind).title;

                String json_payload = String.format(
                        "{\"Messages\":[{\"From\":{\"Email\":\"%s\",\"Name\":\"Software Assignments\"},\"To\":[{\"Email\":\"%s\",\"Name\":\"%s\"}],\"Subject\":\"%s\",\"TextPart\":\"%s\"}]}",
                        from_email, to_email, to_email, subject, message_body
                );

                // Escape the JSON for command line
                json_payload = json_payload.replace("\"", "\\\"");

                List<String> command = new ArrayList<>();
                command.add("curl");
                command.add("-X");
                command.add("POST");
                command.add("https://api.mailjet.com/v3.1/send");
                command.add("-H");
                command.add("Content-Type: application/json");
                command.add("-u");
                command.add(api_key_public + ":" + api_key_private);
                command.add("-d");
                command.add(json_payload);  // Now properly escaped

                System.out.println("\nSending reminder to " + to_email + "...");

                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(command);
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

                    String line;
                    System.out.println("Mailjet response:");
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    int exitCode = process.waitFor();
                    System.out.println("Exited with code: " + exitCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!found) {
            System.out.println("There is no reminders.");
        }
    }

    /**
     * Loads budget data from a file.
     * <p>
     * This method reads budget amount and expenses from a file in the
     * "files" directory. If the file doesn't exist, it creates a new one.
     *
     * @param filename The name of the file to load (without path or extension)
     */
    private void loadData(String filename) {
        File bAndE = new File("./files/" + filename + ".txt");
        try {
            if (bAndE.createNewFile()) {
                System.out.println(Bold + "A new file has been created" + Reset);
            } else {
                System.out.println("A file with this name already exists, loading data");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }

        if (!bAndE.exists()) {
            System.out.println(Bold + Yellow + "No previous data found, starting fresh!!" + Reset);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(bAndE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("budget=")) {
                    budget = Double.parseDouble(line.split("=")[1]);
                } else if (line.contains(":")) {
                    String[] parts = line.split(":");
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    expenses.add(new Expense(amount, category));
                    totalExpense += amount;
                }
            }
            System.out.println("Data loaded successfully!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Saves the current budget data to a file.
     * <p>
     * This method writes the budget amount and all expenses to the specified file.
     *
     * @param filename The full path of the file to save the data to
     */
    public void saveData(String filename) {
        File bAndE = new File(filename);
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("budget=" + budget);
            writer.write("\n");
            for (Expense expense : expenses) {
                writer.write(expense.amount + ":" + expense.category);
                writer.write("\n");
            }
            writer.close();
            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}