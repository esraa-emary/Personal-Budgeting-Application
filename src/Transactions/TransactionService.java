package Transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionService {
    private List<Transaction> transactions;
    private List<Transaction> recurringTransactions;
    private ScheduledExecutorService scheduler;

    public TransactionService() {
        transactions = new ArrayList<>();
        recurringTransactions = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
        startDailyProcessing();
    }

    public void addRecurringTransaction(Transaction transaction) {
        if (transaction.isRecurring()) {
            recurringTransactions.add(transaction);
            transactions.add(transaction);
        }
    }

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

    public boolean cancelRecurringTransaction(int id, String password) {
        for (Transaction rt : recurringTransactions) {
            if (rt.getId() == id && rt.isActive()) {
                rt.setActive(false);
                return true;
            }
        }
        return false;
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getActiveRecurringTransactions() {
        List<Transaction> active = new ArrayList<>();
        for (Transaction rt : recurringTransactions) {
            if (rt.isActive()) {
                active.add(rt);
            }
        }
        return active;
    }

    private void startDailyProcessing() {
        scheduler.scheduleAtFixedRate(() -> {processDueTransactions();}, 0, 1, TimeUnit.DAYS);
    }

    private int generateId() {
        return transactions.size() + 1;
    }
}