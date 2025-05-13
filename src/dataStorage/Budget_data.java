package dataStorage;

import income.*;
import payment.Debt;
import payment.Donate;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for budget information used for JSON serialization and deserialization.
 * <p>
 * This class serves as a bridge between the {@link income.Budget} model and the JSON storage.
 * It contains all budget-related data including budget details, expenses, incomes, goals,
 * reminders, donations, and debts in a format suitable for persistent storage.
 * </p>
 *
 * @author Budget Application Team
 * @version 1.0
 */
public class Budget_data {

    private String budgetId;
    private String budgetName;
    private double budget;
    private double totalExpenses;
    private double totalIncome;
    private double totalDonates;
    private double totalDebts;
    private List<Expense> expenses;
    private List<Income> incomes;
    private List<Goal> goals;
    private List<Reminder> reminders;
    private List<Donate> donates;
    private List<Debt> debts;

    /**
     * Creates a new Budget_data object from a Budget object.
     * <p>
     * This constructor copies all relevant data from a Budget object into this
     * data transfer object for storage in the JSON database.
     * </p>
     *
     * @param budget     The source Budget object containing the budget data
     * @param budgetId   The unique identifier for this budget
     * @param budgetName The display name for this budget
     */
    public Budget_data(Budget budget, String budgetId, String budgetName) {
        this.budgetId = budgetId;
        this.budgetName = budgetName;
        this.budget = budget.getBudget();
        this.totalExpenses = budget.totalExpense;
        this.totalIncome = budget.totalIncome;
        this.totalDonates = budget.totalDonates;
        this.totalDebts = budget.totalDebts;
        this.expenses = new ArrayList<>(budget.expenses);
        this.incomes = new ArrayList<>(budget.incomes);
        this.goals = new ArrayList<>(budget.goals);
        this.reminders = new ArrayList<>(budget.reminders);
        this.donates = new ArrayList<>(budget.donates);
        this.debts = new ArrayList<>(budget.debts);
    }

    /**
     * Converts this data transfer object back to a Budget object.
     * <p>
     * This method creates a new Budget instance and populates it with
     * all the data stored in this Budget_data object.
     * </p>
     *
     * @return A new Budget object populated with data from this Budget_data
     */
    public Budget toBudget() {
        Budget newBudget = new Budget(this.budget);
        newBudget.totalExpense = this.totalExpenses;
        newBudget.totalIncome = this.totalIncome;
        newBudget.totalDonates = this.totalDonates;
        newBudget.totalDebts = this.totalDebts;
        newBudget.expenses = new ArrayList<>(this.expenses);
        newBudget.incomes = new ArrayList<>(this.incomes);
        newBudget.goals = new ArrayList<>(this.goals);
        newBudget.reminders = new ArrayList<>(this.reminders);
        newBudget.donates = new ArrayList<>(this.donates);
        newBudget.debts = new ArrayList<>(this.debts);
        return newBudget;
    }

    /**
     * Gets the unique identifier for this budget.
     *
     * @return The budget ID as a String
     */
    public String getBudgetId() {
        return budgetId;
    }

    /**
     * Sets the unique identifier for this budget.
     *
     * @param budgetId The budget ID to set
     */
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    /**
     * Gets the display name of this budget.
     *
     * @return The budget name as a String
     */
    public String getBudgetName() {
        return budgetName;
    }

    /**
     * Sets the display name of this budget.
     *
     * @param budgetName The budget name to set
     */
    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    /**
     * Gets the total budget amount.
     *
     * @return The budget amount as a double
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Sets the total budget amount.
     *
     * @param budget The budget amount to set
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * Gets the total expenses amount.
     *
     * @return The total expenses as a double
     */
    public double getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * Sets the total expenses amount.
     *
     * @param totalExpenses The total expenses to set
     */
    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    /**
     * Gets the total income amount.
     *
     * @return The total income as a double
     */
    public double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Sets the total income amount.
     *
     * @param totalIncome The total income to set
     */
    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     * Gets the total donations amount.
     *
     * @return The total donations as a double
     */
    public double getTotalDonates() {
        return totalDonates;
    }

    /**
     * Sets the total donations amount.
     *
     * @param totalDonates The total donations to set
     */
    public void setTotalDonates(double totalDonates) {
        this.totalDonates = totalDonates;
    }

    /**
     * Gets the total debts amount.
     *
     * @return The total debts as a double
     */
    public double getTotalDebts() {
        return totalDebts;
    }

    /**
     * Sets the total debts amount.
     *
     * @param totalDebts The total debts to set
     */
    public void setTotalDebts(double totalDebts) {
        this.totalDebts = totalDebts;
    }

    /**
     * Gets all expense entries.
     *
     * @return A list of Expense objects
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets all expense entries.
     *
     * @param expenses A list of Expense objects to set
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Gets all income entries.
     *
     * @return A list of Income objects
     */
    public List<Income> getIncomes() {
        return incomes;
    }

    /**
     * Sets all income entries.
     *
     * @param incomes A list of Income objects to set
     */
    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    /**
     * Gets all financial goal entries.
     *
     * @return A list of Goal objects
     */
    public List<Goal> getGoals() {
        return goals;
    }

    /**
     * Sets all financial goal entries.
     *
     * @param goals A list of Goal objects to set
     */
    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    /**
     * Gets all reminder entries.
     *
     * @return A list of Reminder objects
     */
    public List<Reminder> getReminders() {
        return reminders;
    }

    /**
     * Sets all reminder entries.
     *
     * @param reminders A list of Reminder objects to set
     */
    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    /**
     * Gets all donation entries.
     *
     * @return A list of Donate objects
     */
    public List<Donate> getDonates() {
        return donates;
    }

    /**
     * Sets all donation entries.
     *
     * @param donates A list of Donate objects to set
     */
    public void setDonates(List<Donate> donates) {
        this.donates = donates;
    }

    /**
     * Gets all debt entries.
     *
     * @return A list of Debt objects
     */
    public List<Debt> getDebts() {
        return debts;
    }

    /**
     * Sets all debt entries.
     *
     * @param debts A list of Debt objects to set
     */
    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }
}