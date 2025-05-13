package dataStorage;

import income.*;
import payment.Debt;
import payment.Donate;

import java.util.ArrayList;
import java.util.List;

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

    // Getters and Setters
    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalDonates() {
        return totalDonates;
    }

    public void setTotalDonates(double totalDonates) {
        this.totalDonates = totalDonates;
    }

    public double getTotalDebts() {
        return totalDebts;
    }

    public void setTotalDebts(double totalDebts) {
        this.totalDebts = totalDebts;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public List<Donate> getDonates() {
        return donates;
    }

    public void setDonates(List<Donate> donates) {
        this.donates = donates;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }


}
