package payment;

/**
 * Represents a charitable donation in the Personal Budgeting Application.
 * <p>
 * This class stores information about a user's donation including:
 * <ul>
 *   <li>The amount donated</li>
 *   <li>The recipient organization or cause</li>
 * </ul>
 * <p>
 * Donation objects are created when users record charitable giving and are
 * stored within a Budget object.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see income.Budget
 */
public class Donate {
    /**
     * The recipient organization or cause
     */
    public String source;

    /**
     * The monetary value of the donation
     */
    public double amount;

    /**
     * Creates a new donation with the specified amount and recipient.
     *
     * @param amount The monetary value of the donation
     * @param source The recipient organization or cause
     */
    public Donate(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }
}