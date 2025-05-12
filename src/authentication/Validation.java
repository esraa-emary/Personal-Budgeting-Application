package authentication;

import java.util.regex.*;

/**
 * Provides validation utilities for user input in the Personal Budgeting Application.
 * <p>
 * This class contains static methods that validate:
 * <ul>
 *   <li>Email addresses - ensuring proper format with username, @ symbol, and domain</li>
 *   <li>Passwords - enforcing minimum security requirements</li>
 * </ul>
 * <p>
 * All validation is performed using regular expression pattern matching.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Login
 * @see Signup
 */
public class Validation {

    /**
     * Regular expression pattern for validating email addresses.
     * <p>
     * Ensures emails follow the standard format: username@domain.tld
     * The pattern allows alphanumeric characters, plus common symbols in the username,
     * followed by @ symbol, domain name, and a top-level domain between 2-6 characters.
     */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    /**
     * Regular expression pattern for validating passwords.
     * <p>
     * Ensures passwords:
     * <ul>
     *   <li>Are at least 6 characters long</li>
     *   <li>Contain only alphanumeric characters and allowed symbols</li>
     * </ul>
     * Allowed symbols include: +, _, ., !, @, #, $, %, ^, &, *, -
     */
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9+_.!@#$%^&*-]{6,}$";

    /**
     * Validates whether the provided email address is properly formatted.
     *
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Validates whether the provided password meets the security requirements.
     * <p>
     * A valid password must be at least 6 characters long and contain only
     * alphanumeric characters and allowed special symbols.
     *
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * Validates whether the provided creation date is in a valid format.
     * <p>
     * This method checks if the `createdAt` string matches the expected pattern.
     * Note: The current implementation is incorrect and needs to be updated
     * to use a proper regular expression for date validation.
     *
     * @param createdAt The creation date string to validate
     * @return true if the creation date is valid, false otherwise
     */

    private static final String NUMBER_REGEX = "^[0-9]*$";

    public static boolean isValidNumber(String number) {
        return Pattern.matches(NUMBER_REGEX, number);
    }

    private static final String AMOUNT_REGEX = "^\\d+\\.\\d+|\\d+\\.?\\d*$";

    public static boolean isValidAmount(String amount) {
        return Pattern.matches(AMOUNT_REGEX, amount);
    }

    private static final String DATE_REGEX = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])|(?:0[13-9]|1[0-2])-30|(?:0[13578]|1[02])-31))$";

    public static boolean isValidDate(String date) {
        return Pattern.matches(DATE_REGEX, date);
    }
}