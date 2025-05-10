package income;

import static run.Format.*;

import java.io.*;
import java.util.ArrayList;

public class Budget {
    public double budget;
    public ArrayList<Expense> expenses = new ArrayList<>();
    public double totalExpense = 0;
    public ArrayList<Income> incomes = new ArrayList<>();
    public double totalIncome = 0;
    public ArrayList<Goal> goals = new ArrayList<>();
    public ArrayList<Reminder> reminders = new ArrayList<>();

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

    private void loadData(String filename) {
        File bAndE = new File(filename);
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
