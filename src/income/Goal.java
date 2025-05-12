package income;

/**
 * Represents a financial goal in the Personal Budgeting Application.
 * <p>
 * This class stores information about a user's financial goal including:
 * <ul>
 *   <li>The target amount to be saved or accumulated</li>
 *   <li>The target date by which the goal should be achieved</li>
 * </ul>
 * <p>
 * Goals help users track progress toward specific financial objectives
 * such as saving for a vacation, paying off debt, or building an emergency fund.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Budget
 */
public class Goal {
    /** The target date by which the goal should be achieved (format: YYYY-MM-DD) */
    public String date;

    /** The target monetary amount for the goal */
    public double amount;

    /**
     * Creates a new financial goal with the specified amount and target date.
     *
     * @param amount The target monetary amount for the goal
     * @param date The target date by which the goal should be achieved
     */
    public Goal(double amount, String date) {
        this.amount = amount;
        this.date = date;
    }
}