package Transactions;

import java.time.LocalDate;

/**
 * Represents a financial transaction in the Personal Budgeting Application.
 * <p>
 * This class models both one-time and recurring transactions with properties for:
 * <ul>
 *   <li>Transaction metadata (ID, description, category)</li>
 *   <li>Financial information (amount, income/expense type)</li>
 *   <li>Timing details (date, recurrence pattern)</li>
 * </ul>
 * <p>
 * For recurring transactions, it provides functionality to calculate
 * the next occurrence date based on frequency and end date constraints.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see TransactionService
 * @see TransactionController
 */
public class Transaction {
    /**
     * Unique identifier for the transaction
     */
    private int id;

    /**
     * Description or purpose of the transaction
     */
    private String description;

    /**
     * Monetary value of the transaction
     */
    private double amount;

    /**
     * Category or type of the transaction
     */
    private String category;

    /**
     * Date when the transaction occurs/occurred
     */
    private LocalDate date;

    /**
     * Flag indicating whether this is income (true) or expense (false)
     */
    private boolean isIncome;

    /**
     * Flag indicating whether this is a recurring transaction
     */
    private boolean isRecurring;

    /**
     * Frequency pattern for recurring transactions (daily, weekly, monthly, yearly)
     */
    private String frequency;

    /**
     * Optional end date for recurring transactions
     */
    private LocalDate endDate;

    /**
     * Flag indicating whether this recurring transaction is still active
     */
    private boolean isActive;

    /**
     * Creates a new one-time transaction.
     *
     * @param id          Unique identifier for the transaction
     * @param description Description or purpose of the transaction
     * @param amount      Monetary value of the transaction
     * @param category    Category or type of the transaction
     * @param date        Date when the transaction occurs/occurred
     * @param isIncome    Flag indicating whether this is income (true) or expense (false)
     */
    public Transaction(int id, String description, double amount, String category,
                       LocalDate date, boolean isIncome) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isIncome = isIncome;
        this.isRecurring = false;
        this.isActive = true;
    }

    /**
     * Creates a new recurring transaction.
     *
     * @param id          Unique identifier for the transaction
     * @param description Description or purpose of the transaction
     * @param amount      Monetary value of the transaction
     * @param category    Category or type of the transaction
     * @param date        Date of the first occurrence
     * @param isIncome    Flag indicating whether this is income (true) or expense (false)
     * @param frequency   Recurrence pattern (daily, weekly, monthly, yearly)
     * @param endDate     Optional end date for the recurring transaction, or null if indefinite
     */
    public Transaction(int id, String description, double amount, String category,
                       LocalDate date, boolean isIncome, String frequency,
                       LocalDate endDate) {
        this(id, description, amount, category, date, isIncome);
        this.isRecurring = true;
        this.frequency = frequency;
        this.endDate = endDate;
    }

    /**
     * Gets the transaction's unique identifier.
     *
     * @return The transaction ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the transaction's description.
     *
     * @return The transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the transaction's monetary amount.
     *
     * @return The transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the transaction's category.
     *
     * @return The transaction category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the transaction's date.
     *
     * @return The transaction date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets a new date for the transaction.
     * <p>
     * Used primarily for updating recurring transactions.
     *
     * @param date The new date for the transaction
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Checks if this is an income transaction.
     *
     * @return true if income, false if expense
     */
    public boolean isIncome() {
        return isIncome;
    }

    /**
     * Checks if this is a recurring transaction.
     *
     * @return true if recurring, false if one-time
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Gets the recurrence frequency for recurring transactions.
     *
     * @return The frequency pattern (daily, weekly, monthly, yearly)
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Gets the end date for recurring transactions.
     *
     * @return The end date, or null if indefinite
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Checks if this recurring transaction is still active.
     *
     * @return true if active, false if canceled or completed
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of a recurring transaction.
     * <p>
     * Used to cancel or complete recurring transactions.
     *
     * @param active The new active status
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Calculates the next occurrence date for a recurring transaction.
     * <p>
     * This method determines when the transaction will next occur based on:
     * <ul>
     *   <li>The recurrence frequency (daily, weekly, monthly, yearly)</li>
     *   <li>The current/last occurrence date</li>
     *   <li>The optional end date constraint</li>
     * </ul>
     * <p>
     * For past occurrences, it advances the date until finding the next future occurrence.
     *
     * @return The next occurrence date, or null if no future occurrences (end date passed)
     */
    public LocalDate getNextOccurrence() {
        if (!isRecurring)
            return null;

        LocalDate nextDate = date;
        LocalDate today = LocalDate.now();

        while (nextDate.isBefore(today) || nextDate.isEqual(today)) {
            switch (frequency) {
                case "daily":
                    nextDate = nextDate.plusDays(1);
                    break;
                case "weekly":
                    nextDate = nextDate.plusWeeks(1);
                    break;
                case "monthly":
                    nextDate = nextDate.plusMonths(1);
                    break;
                case "yearly":
                    nextDate = nextDate.plusYears(1);
                    break;
            }

            if (endDate != null && nextDate.isAfter(endDate)) {
                return null; // No more occurrences
            }
        }

        return nextDate;
    }
}