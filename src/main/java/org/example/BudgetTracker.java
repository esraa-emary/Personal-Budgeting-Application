package org.example;

import org.example.Expense;
import org.example.cliFormat;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.ArrayList;
import static org.example.cliFormat.*;

public class BudgetTracker {

    double totalExpense;
    ArrayList<Expense> expenses = new ArrayList<>();
    private double budget;

    public BudgetTracker(double budget) {
        this.budget = budget;
    }

    public BudgetTracker(String filename) {

        loadData(filename);
    }

    void addExpense(double amount, String category) {

        if (totalExpense + amount > budget) {

            System.out.println("Exceeded budget, you cannot add this expense!");
        } else {
            expenses.add(new Expense(amount, category));
            totalExpense += amount;
            System.out.println("Expense added: " + "$" + Bold + Blue + amount + Reset + " for " + Bold + Blue + category + Reset);

        }

    }


    void setBudget(double budget) {
        this.budget = budget;
    }

    double getBudget() {
        return budget;
    }

    double getTotalExpense() {
        return totalExpense;
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
