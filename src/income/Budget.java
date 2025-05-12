package income;

import static run.Format.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

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
     * Sum of all income
     */
    public double totalIncome = 0;

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
     * @param filename The name of the file to load budget data from
     * @see #loadData(String)
     */
    public Budget(String filename) {
        loadData(filename);
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
     * Retrieves the current budget amount.
     *
     * @return The current budget amount
     */
    public double getBudget() {
        return budget;
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
        }
    }

    /**
     * Adds a new income source to the budget.
     *
     * @param amount The income amount
     * @param source The source of the income
     */
    public void addIncome(double amount, String source) {
        incomes.add(new Income(amount, source));
        totalIncome += amount;
        System.out.println("Income added: " + "$" + Bold + Blue + amount + Reset + " from " + Bold + Blue + source + Reset);
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
        if (totalDonates + amount > budget) {
            System.out.println("Exceeded budget, you cannot add this donate!");
        } else {
            donates.add(new Donate(amount, source));
            totalDonates += amount;
            System.out.println("Donate added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
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
        if (totalDebts + amount > budget) {
            System.out.println("Exceeded budget, you cannot repayment this debt!");
        } else {
            debts.add(new Debt(amount, source));
            totalDebts += amount;
            System.out.println("Debt added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
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