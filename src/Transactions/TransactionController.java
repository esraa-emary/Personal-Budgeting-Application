package Transactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static run.Format.*;

public class TransactionController {
    private TransactionService transactionService;
    private Scanner scanner;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

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