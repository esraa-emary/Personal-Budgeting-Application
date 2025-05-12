package income;

/**
 * Represents an income entry in the Personal Budgeting Application.
 * <p>
 * This class stores information about a single income transaction including:
 * <ul>
 *   <li>The amount received</li>
 *   <li>The source of the income (e.g., salary, freelance, investments)</li>
 * </ul>
 * <p>
 * Income objects are created when users record money received and are
 * stored within a Budget object to track total earnings.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Budget
 */
public class Income {
    /** The source or origin of the income */
    public String source;

    /** The monetary value of the income */
    public double amount;

    /**
     * Creates a new income entry with the specified amount and source.
     *
     * @param amount The monetary value of the income
     * @param source The source or origin of the income
     */
    public Income(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }
}