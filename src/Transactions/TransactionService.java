package Transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service class for managing transactions in the Personal Budgeting Application.
 * <p>
 * This class provides the core functionality for:
 * <ul>
 *   <li>Adding and tracking transactions</li>
 *   <li>Managing recurring transactions</li>
 *   <li>Automatically processing due transactions</li>
 *   <li>Canceling scheduled recurring transactions</li>
 * </ul>
 * <p>
 * The service maintains two collections: all transactions and recurring transactions,
 * and uses a scheduled executor to automatically process recurring transactions daily.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Transaction
 * @see TransactionController
 */
public class TransactionService {
    /**
     * Collection of all transactions (one-time and recurring)
     */
    private List<Transaction> transactions;

    /**
     * Collection of recurring transactions only
     */
    public List<Transaction> recurringTransactions;

    /**
     * Scheduler for processing recurring transactions
     */
    private ScheduledExecutorService scheduler;

    /**
     * Creates a new transaction service and starts the daily processing scheduler.
     * <p>
     * This constructor initializes the transaction collections and sets up
     * the scheduled task to process recurring transactions daily.
     */
    public TransactionService() {
        transactions = new ArrayList<>();
        recurringTransactions = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
        startDailyProcessing();
    }

    /**
     * Adds a new recurring transaction to the service.
     * <p>
     * The transaction is added to both the main transactions list and
     * the recurring transactions list if it has a recurrence pattern.
     *
     * @param transaction The recurring transaction to add
     */
    public void addRecurringTransaction(Transaction transaction) {
        if (transaction.isRecurring()) {
            recurringTransactions.add(transaction);
            transactions.add(transaction);
        }
    }

    /**
     * Processes all due recurring transactions for the current day.
     * <p>
     * This method:
     * <ol>
     *   <li>Checks all recurring transactions for today's date</li>
     *   <li>Creates new transaction instances for each occurrence</li>
     *   <li>Updates recurring transactions to their next occurrence dates</li>
     *   <li>Deactivates completed recurring transactions</li>
     * </ol>
     * <p>
     * This is automatically called daily by the scheduler.
     */
    public void processDueTransactions() {
        LocalDate today = LocalDate.now();
        List<Transaction> transactionsToAdd = new ArrayList<>();

        for (Transaction rt : recurringTransactions) {
            if (rt.isActive() && rt.getDate().isEqual(today)) {
                // Create a new transaction instance for this occurrence
                Transaction occurrence = new Transaction(
                        generateId(),
                        rt.getDescription(),
                        rt.getAmount(),
                        rt.getCategory(),
                        today,
                        rt.isIncome()
                );
                transactionsToAdd.add(occurrence);

                // Update next occurrence date
                LocalDate nextDate = rt.getNextOccurrence();
                if (nextDate == null) {
                    rt.setActive(false);
                } else {
                    rt.setDate(nextDate);
                }
            }
        }

        transactions.addAll(transactionsToAdd);
    }

    /**
     * Cancels an active recurring transaction.
     * <p>
     * This method deactivates a recurring transaction, preventing future occurrences.
     * A password is required for security verification.
     *
     * @param id       The ID of the recurring transaction to cancel
     * @return true if successfully canceled, false if not found or already inactive
     */
    public boolean cancelRecurringTransaction(int id) {
        for (Transaction rt : recurringTransactions) {
            if (rt.getId() == id && rt.isActive()) {
                rt.setActive(false);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all transactions in the system.
     *
     * @return A new list containing all transactions (one-time and recurring)
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Retrieves only active recurring transactions.
     *
     * @return A list of all active recurring transactions
     */
    public List<Transaction> getActiveRecurringTransactions() {
        List<Transaction> active = new ArrayList<>();
        for (Transaction rt : recurringTransactions) {
            if (rt.isActive()) {
                active.add(rt);
            }
        }
        return active;
    }

    /**
     * Starts the scheduled daily processing of recurring transactions.
     * <p>
     * This private method is called during initialization to set up
     * the daily task that processes recurring transactions.
     */
    private void startDailyProcessing() {
        scheduler.scheduleAtFixedRate(() -> {
            processDueTransactions();
        }, 0, 1, TimeUnit.DAYS);
    }

    /**
     * Generates a unique ID for new transactions.
     * <p>
     * The generated ID is simply the next sequential number based on
     * the current number of transactions.
     *
     * @return A new unique transaction ID
     */
    private int generateId() {
        return transactions.size() + 1;
    }
}
