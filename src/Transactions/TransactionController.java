package Transactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static run.Format.*;

/**
 * Controller for managing transaction-related user interactions in the Personal Budgeting Application.
 * <p>
 * This class provides the user interface for:
 * <ul>
 *   <li>Adding recurring transactions with various frequencies</li>
 *   <li>Viewing active recurring transactions</li>
 *   <li>Canceling unwanted recurring transactions</li>
 * </ul>
 * <p>
 * The controller handles user input validation, data formatting, and delegates
 * business logic to the TransactionService.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see TransactionService
 * @see Transaction
 */
public class TransactionController {
    /**
     * Service for handling transaction business logic
     */
    private TransactionService transactionService;

    /**
     * Scanner for reading user input
     */
    private Scanner scanner;

    /**
     * Creates a new transaction controller with the specified service.
     *
     * @param transactionService The service to use for transaction operations
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu for recurring transactions management.
     * <p>
     * This method presents options for:
     * <ul>
     *   <li>Adding new recurring transactions</li>
     *   <li>Viewing active recurring transactions</li>
     *   <li>Canceling existing recurring transactions</li>
     *   <li>Returning to the previous menu</li>
     * </ul>
     * <p>
     * It handles user input and delegates to appropriate methods based on selection.
     */
    public void showRecurringTransactionMenu() {
        while (true) {
            System.out.println(Bold + Cyan + "\n<------- Recurring Transactions Department\n" + Reset);
            System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
            System.out.println(Bold + "1 -> Add Recurring Transaction" + Reset);
            System.out.println(Bold + "2 -> View Active Recurring Transactions" + Reset);
            System.out.println(Bold + "3 -> Cancel Recurring Transaction" + Reset);
            System.out.println(Bold + Red + "4 -> Back." + Reset);
            System.out.print(Bold + "Choose an option: " + Reset);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addRecurringTransaction();
                    break;
                case 2:
                    viewActiveRecurringTransactions();
                    break;
                case 3:
                    cancelRecurringTransaction();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Collects user input for creating a new recurring transaction.
     * <p>
     * This method guides the user through entering:
     * <ul>
     *   <li>Basic transaction details (description, amount, category)</li>
     *   <li>Transaction type (income or expense)</li>
     *   <li>Recurrence settings (start date, frequency, optional end date)</li>
     * </ul>
     * <p>
     * Input validation is performed for numerical values and date formats.
     */
    private void addRecurringTransaction() {
        System.out.println("\nAdd New Recurring Transaction");

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = 0;
        try {
            amount = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount. Please enter a number.");
            scanner.nextLine(); // clear invalid input
            return;
        }
        scanner.nextLine();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        System.out.println("\nTransaction Type:");
        System.out.println("1 -> Income");
        System.out.println("2 -> Expense");
        System.out.print("Choose type: ");
        boolean isIncome = false;
        try {
            int typeChoice = scanner.nextInt();
            scanner.nextLine();
            if (typeChoice == 1) {
                isIncome = true;
            } else if (typeChoice != 2) {
                System.out.println("Invalid choice. Defaulting to Expense.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Defaulting to Expense.");
            scanner.nextLine();
        }

        System.out.print("\nStart date (YYYY-MM-DD): ");
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.println("\nFrequency:");
        System.out.println("1 -> Daily");
        System.out.println("2 -> Weekly");
        System.out.println("3 -> Monthly");
        System.out.println("4 -> Yearly");
        System.out.print("Choose frequency: ");
        String frequency;
        try {
            int freqChoice = scanner.nextInt();
            scanner.nextLine();
            switch (freqChoice) {
                case 1:
                    frequency = "daily";
                    break;
                case 2:
                    frequency = "weekly";
                    break;
                case 3:
                    frequency = "monthly";
                    break;
                case 4:
                    frequency = "yearly";
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to monthly.");
                    frequency = "monthly";
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid choice. Defaulting to monthly.");
            frequency = "monthly";
            scanner.nextLine();
        }

        System.out.print("End date (YYYY-MM-DD or leave empty): ");
        String endDateInput = scanner.nextLine();
        LocalDate endDate = null;
        if (!endDateInput.isEmpty()) {
            try {
                endDate = LocalDate.parse(endDateInput, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Setting no end date.");
            }
        }

        Transaction transaction = new Transaction(
                transactionService.getAllTransactions().size() + 1,
                description,
                amount,
                category,
                startDate,
                isIncome,
                frequency,
                endDate
        );

        transactionService.addRecurringTransaction(transaction);
        System.out.println("Recurring transaction added successfully!");
    }

    /**
     * Displays all active recurring transactions.
     * <p>
     * This method retrieves active recurring transactions from the service
     * and presents them in a formatted table showing:
     * <ul>
     *   <li>Transaction ID</li>
     *   <li>Description</li>
     *   <li>Amount</li>
     *   <li>Category</li>
     *   <li>Next occurrence date</li>
     * </ul>
     */
    private void viewActiveRecurringTransactions() {
        List<Transaction> activeRecurring = transactionService.getActiveRecurringTransactions();

        if (activeRecurring.isEmpty()) {
            System.out.println("No active recurring transactions.");
            return;
        }

        System.out.println("\nActive Recurring Transactions:");
        for (Transaction rt : activeRecurring) {
            System.out.printf("ID: %d | %s | Amount: %.2f | Category: %s | Next: %s%n",
                    rt.getId(),
                    rt.getDescription(),
                    rt.getAmount(),
                    rt.getCategory(),
                    rt.getNextOccurrence());
        }
    }

    /**
     * Handles the cancellation of a recurring transaction.
     * <p>
     * This method:
     * <ol>
     *   <li>Displays active recurring transactions</li>
     *   <li>Prompts for the ID of the transaction to cancel</li>
     *   <li>Requires password verification for security</li>
     *   <li>Delegates to the service to perform the cancellation</li>
     * </ol>
     */
    private void cancelRecurringTransaction() {
        viewActiveRecurringTransactions();

        if (transactionService.getActiveRecurringTransactions().isEmpty()) {
            return;
        }

        System.out.print("Enter ID of transaction to cancel: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter your password to confirm: ");
        String password = scanner.nextLine();

        if (transactionService.cancelRecurringTransaction(id, password)) {
            System.out.println("Transaction cancelled successfully.");
        } else {
            System.out.println("Failed to cancel transaction. Invalid ID or already cancelled.");
        }
    }
}