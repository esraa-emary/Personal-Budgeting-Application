package run;

import static run.Format.*;

import Transactions.Transaction;
import Transactions.TransactionController;
import Transactions.TransactionService;
import income.Budget;
import income.Income;
import income.Expense;
import income.Goal;
import income.Reminder;
import payment.Debt;
import payment.Donate;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Menu {
    public static boolean isContinue = true;
    public static boolean isMain = true;
    public static Scanner input = new Scanner(System.in);

    public static int displayMainMenuAuthentication() {
        System.out.println(Bold + Cyan + "<------- Welcome To our Personal Budgeting Application ------->\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Log-In." + Reset);
        System.out.println(Bold + "2 -> Sign-Up." + Reset);
        System.out.println(Bold + Red + "3 -> Exit." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static int displayMainMenuSections() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To our Personal Budgeting Application\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Income Section." + Reset);
        System.out.println(Bold + "2 -> Payment Section." + Reset);
        System.out.println(Bold + Red + "3 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static int displayMainMenuIncome() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To Income Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Add Budget." + Reset);
        System.out.println(Bold + "2 -> Add Expense." + Reset);
        System.out.println(Bold + "3 -> Add Income." + Reset);
        System.out.println(Bold + "4 -> Add Goal." + Reset);
        System.out.println(Bold + "5 -> Add Reminder." + Reset);

        System.out.println(Bold + "6 -> Display Budgets." + Reset);
        System.out.println(Bold + "7 -> Display Expenses." + Reset);
        System.out.println(Bold + "8 -> Display Incomes." + Reset);
        System.out.println(Bold + "9 -> Display Goals." + Reset);
        System.out.println(Bold + "10 -> Display Reminders." + Reset);

        System.out.println(Bold + "11 -> Send Reminder." + Reset);

        System.out.println(Bold + Red + "12 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static int displayMainMenuPayment() {
        System.out.println(Bold + Cyan + "\n<------- Welcome To Payment Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Transactions Department." + Reset);
        System.out.println(Bold + "2 -> Add Debt Repayment." + Reset);
        System.out.println(Bold + "3 -> Add Donate." + Reset);
        System.out.println(Bold + "4 -> Display Debts Repayment." + Reset);
        System.out.println(Bold + "5 -> Display Donates." + Reset);
        System.out.println(Bold + "6 -> Financial Reports." + Reset);
        System.out.println(Bold + Red + "7 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static void optionsIncome(int innerChoice, Budget bt, String fileName, Scanner external_input) {
        String source, date, title;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                boolean case1_valid = false;
                while (!case1_valid) {
                    try {
                        System.out.print(Bold + "Please enter your" + Green + " budget" + Reset + Bold + " amount: " + Reset);
                        double bud_amount2 = input.nextDouble();
                        bt.setBudget(bud_amount2);
                        bt.saveData(fileName);
                        case1_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }
                break;

            case 2:
                double am = 0;
                boolean case2_valid = false;
                while (!case2_valid) {
                    try {
                        System.out.print("How much did you pay: ");
                        am = external_input.nextDouble();
                        case2_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.next();
                    }
                }
                System.out.print("What did you exactly pay for: ");
                String cat = external_input.next();
                bt.addExpense(am, cat);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.print("Enter Source of income: ");
                source = external_input.next();
                System.out.print("How much is the amount of income: ");
                amount = external_input.nextDouble();

                bt.addIncome(amount, source);
                bt.saveData(fileName);

                Iterator<Goal> iterator = bt.goals.iterator();
                while (iterator.hasNext()) {
                    Goal g = iterator.next();
                    if (bt.totalIncome - bt.totalExpense >= g.amount) {
                        System.out.println(Bold + Red + "You got your goal: " + g.amount + "$" + Reset);
                        iterator.remove();
                    }
                }
                break;

            case 4:
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = external_input.next();
                System.out.print("How much is the amount of money is your goal by then: ");
                amount = external_input.nextDouble();

                bt.addGoal(amount, date);
                bt.saveData(fileName);
                break;

            case 5:
                System.out.print("Enter date you want as (YYYY-MM-DD): ");
                date = external_input.next();
                System.out.print("What is the title of this reminder: ");
                title = external_input.next();

                bt.addReminder(title, date);
                bt.saveData(fileName);
                break;

            case 6:
                System.out.println("Your still have from you budget: " + Bold + Green + (bt.getBudget() - bt.totalExpense - bt.totalDonates - bt.totalDebts) + "$" + Reset);
                System.out.println("For expenses: " + Bold + Green + (bt.totalExpense) + "$" + Reset);
                System.out.println("For donates: " + Bold + Green + (bt.totalDonates) + "$" + Reset);
                System.out.println("For debts: " + Bold + Green + (bt.totalDebts) + "$" + Reset);
                System.out.println("You still have from you income: " + Bold + Green + (bt.totalIncome - bt.totalExpense) + "$" + Reset);
                break;

            case 7:
                System.out.println("Your" + Bold + Red + " Expenses" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Expense e : bt.expenses) {
                    System.out.println(Bold + counter + "- " + Red + e.amount + "$" + Reset + " for " + Bold + Blue + e.category + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalExpense + Reset + "\n");
                break;

            case 8:
                System.out.println("Your" + Bold + Red + " Income" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Income i : bt.incomes) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalIncome + Reset + "\n");
                break;

            case 9:
                System.out.println("Your" + Bold + Red + " Goals" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Goal g : bt.goals) {
                    System.out.println(Bold + counter + "- get " + Red + g.amount + "$" + Reset + " by " + Bold + Blue + g.date + Reset);
                    counter++;
                }
                break;

            case 10:
                System.out.println("Your" + Bold + Red + " Reminders" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Reminder r : bt.reminders) {
                    System.out.println(Bold + counter + "- reminder about " + Red + r.title + Reset + " on " + Bold + Blue + r.date + Reset);
                    counter++;
                }
                break;

            case 11:
                bt.sendReminder(bt, external_input);
                break;

            case 12:
                isContinue = false;
                isMain = false;
                System.out.println(Purple + Bold + "Now I guess it's time to save some bytes, just like we save some" + Green + " cash" + Reset + Bold + Purple + ". Goodbye!" + Reset);
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 8.");
                break;
        }
    }

    public static void optionsPayment(int innerChoice, Budget bt, String fileName, Scanner external_input, TransactionController tc) {
        String source;
        double amount;
        int counter;
        switch (innerChoice) {
            case 1:
                tc.showRecurringTransactionMenu();
                break;

            case 2:
                System.out.print("Enter source of debt: ");
                source = external_input.next();
                System.out.print("How much is the amount of debt: ");
                amount = external_input.nextDouble();

                bt.addDebt(amount, source);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.print("Enter source to donate: ");
                source = external_input.next();
                System.out.print("How much is the amount of money: ");
                amount = external_input.nextDouble();

                bt.addDonate(amount, source);
                bt.saveData(fileName);
                break;

            case 4:
                System.out.println("Your" + Bold + Red + " Debts" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Debt r : bt.debts) {
                    System.out.println(Bold + counter + "- debt for " + Red + r.source + Reset + " with " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }
                break;

            case 5:
                System.out.println("Your" + Bold + Red + " Donates" + Reset + Bold + " are:" + Reset);

                counter = 1;
                for (Donate r : bt.donates) {
                    System.out.println(Bold + counter + "- donate to " + Red + r.source + Reset + " by " + Bold + Blue + r.amount + "$" + Reset);
                    counter++;
                }
                break;

            case 6:
                // income
                System.out.println("Your total income is: " + Bold + Green + (bt.totalIncome) + "$\n" + Reset + "Which comes from:");
                counter = 1;
                for (Income i : bt.incomes) {
                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
                    counter++;
                }

                // budget
                System.out.println("Your budget is: " + Bold + Green + (bt.budget) + "$\n" + Reset);

                // transaction
                System.out.println("Your transactions come from:");
                counter = 1;
//                for (Transaction t : tc.) {
//                    System.out.println(Bold + counter + "- " + Red + i.amount + "$" + Reset + " from " + Bold + Blue + i.source + Reset);
//                    counter++;
//                }
                break;

            case 7:
                isContinue = false;
                isMain = false;
                System.out.println(Purple + Bold + "Now I guess it's time to save some bytes, just like we save some" + Green + " cash" + Reset + Bold + Purple + ". Goodbye!" + Reset);
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 6.");
        }
    }
}
