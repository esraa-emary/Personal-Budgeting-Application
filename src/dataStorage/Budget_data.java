package dataStorage;

import income.Expense;
import income.Goal;
import income.Income;
import income.Reminder;
import payment.Debt;
import payment.Donate;
import java.util.List;

public class Budget_data {

    private String budgetId;
    private String budgetName;
    private double budgetAmount;
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
}
