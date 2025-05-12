package income;

/**
 * Represents an individual expense in the Personal Budgeting Application.
 * <p>
 * This class stores information about a single expense including:
 * <ul>
 *   <li>The amount spent</li>
 *   <li>The category of the expense (e.g., groceries, utilities, entertainment)</li>
 * </ul>
 * <p>
 * Expense objects are created when users record their spending and are
 * stored within a Budget object.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Budget
 */
public class Expense {
    /** The category or purpose of the expense */
    public String category;

    /** The monetary value of the expense */
    public double amount;

    /**
     * Creates a new expense with the specified amount and category.
     *
     * @param amount The monetary value of the expense
     * @param category The category or purpose of the expense
     */
    public Expense(double amount, String category) {
        this.amount = amount;
        this.category = category;
    }
}