package payment;

/**
 * Represents a debt repayment in the Personal Budgeting Application.
 * <p>
 * This class stores information about a user's debt repayment including:
 * <ul>
 *   <li>The amount being repaid</li>
 *   <li>The creditor or source of the debt</li>
 * </ul>
 * <p>
 * Debt objects are created when users record debt repayments and are
 * stored within a Budget object.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see income.Budget
 */
public class Debt {
    /** The creditor or source of the debt */
    public String source;

    /** The monetary value of the debt repayment */
    public double amount;

    /**
     * Creates a new debt repayment with the specified amount and source.
     *
     * @param amount The monetary value of the debt repayment
     * @param source The creditor or source of the debt
     */
    public Debt(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }
}