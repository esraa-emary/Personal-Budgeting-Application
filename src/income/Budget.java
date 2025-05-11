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

public class Budget {
    public double budget;
    public ArrayList<Expense> expenses = new ArrayList<>();
    public double totalExpense = 0;
    public ArrayList<Income> incomes = new ArrayList<>();
    public double totalIncome = 0;
    public ArrayList<Goal> goals = new ArrayList<>();
    public ArrayList<Reminder> reminders = new ArrayList<>();

    public ArrayList<Donate> donates = new ArrayList<>();
    public double totalDonates = 0;

    public ArrayList<Debt> debts = new ArrayList<>();
    public double totalDebts = 0;

    private static final String USERS_DB_FILE_PATH = "files/users_db.json";
    private String mailReminder = "", usernameReminder = "";

    public Budget(double budget) {
        this.budget = budget;
    }

    public Budget(String filename) {
        loadData(filename);
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBudget() {
        return budget;
    }

    public void addExpense(double amount, String category) {
        if (totalExpense + amount > budget) {
            System.out.println("Exceeded budget, you cannot add this expense!");
        } else {
            expenses.add(new Expense(amount, category));
            totalExpense += amount;
            System.out.println("Expense added: " + "$" + Bold + Blue + amount + Reset + " for " + Bold + Blue + category + Reset);
        }
    }

    public void addIncome(double amount, String source) {
        incomes.add(new Income(amount, source));
        totalIncome += amount;
        System.out.println("Income added: " + "$" + Bold + Blue + amount + Reset + " from " + Bold + Blue + source + Reset);
    }

    public void addGoal(double amount, String date) {
        goals.add(new Goal(amount, date));
        System.out.println("Goal added: " + "$" + Bold + Blue + amount + Reset + " by " + Bold + Blue + date + Reset);
    }

    public void addReminder(String title, String date) {
        reminders.add(new Reminder(title, date));
        System.out.println("Reminder added: " + "$" + Bold + Blue + title + Reset + " on " + Bold + Blue + date + Reset);
    }

    public void addDonate(double amount, String source) {
        if (totalDonates + amount > budget) {
            System.out.println("Exceeded budget, you cannot add this donate!");
        } else {
            donates.add(new Donate(amount, source));
            totalDonates += amount;
            System.out.println("Donate added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
        }
    }

    public void addDebt(double amount, String source) {
        if (totalDebts + amount > budget) {
            System.out.println("Exceeded budget, you cannot repayment this debt!");
        } else {
            debts.add(new Debt(amount, source));
            totalDebts += amount;
            System.out.println("Debt added: " + "$" + Bold + Blue + amount + Reset + " to " + Bold + Blue + source + Reset);
        }
    }

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
