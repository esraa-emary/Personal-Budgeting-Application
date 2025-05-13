package income;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static run.Format.*;

/**
 * Manages income data across the application, independent of specific budgets.
 * <p>
 * This class provides centralized storage and operations for all income entries
 * that can be shared and utilized by various budget objects.
 *
 * @author Budget Application Team
 * @version 1.0
 */
public class Income_manager {
    /** Maps user IDs to their income entries */
    private static Map<String, List<Income>> userIncomes = new HashMap<>();

    /** Maps user IDs to their total income amount */
    private static Map<String, Double> userTotalIncome = new HashMap<>();

    /**
     * Adds a new income source for a user.
     *
     * @param userId The ID of the user
     * @param amount The income amount
     * @param source The source of the income
     * @return The newly created Income object
     */
    public static Income addIncome(String userId, double amount, String source) {
        Income income = new Income(amount, source);

        List<Income> incomes = userIncomes.getOrDefault(userId, new ArrayList<>());
        incomes.add(income);
        userIncomes.put(userId, incomes);

        double total = userTotalIncome.getOrDefault(userId, 0.0) + amount;
        userTotalIncome.put(userId, total);

        System.out.println("Income added: " + "$" + Bold + Blue + amount + Reset + " from " + Bold + Blue + source + Reset);
        return income;
    }

    /**
     * Gets all income entries for a user.
     *
     * @param userId The ID of the user
     * @return List of Income objects for the user
     */
    public static List<Income> getUserIncomes(String userId) {
        return userIncomes.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Gets the total income amount for a user.
     *
     * @param userId The ID of the user
     * @return The total income amount
     */
    public static double getTotalIncome(String userId) {
        return userTotalIncome.getOrDefault(userId, 0.0);
    }
}