package Transactions;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String description;
    private double amount;
    private String category;
    private LocalDate date;
    private boolean isIncome;
    private boolean isRecurring;
    private String frequency;
    private LocalDate endDate;
    private boolean isActive;

    // Constructor for one-time transactions
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

    // Constructor for recurring transactions
    public Transaction(int id, String description, double amount, String category,
                       LocalDate date, boolean isIncome, String frequency,
                       LocalDate endDate) {
        this(id, description, amount, category, date, isIncome);
        this.isRecurring = true;
        this.frequency = frequency;
        this.endDate = endDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isIncome() { return isIncome; }
    public boolean isRecurring() { return isRecurring; }
    public String getFrequency() { return frequency; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Calculate next occurrence date for recurring transactions
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