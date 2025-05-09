package run;

import static run.Format.*;

import income.Budget;
import income.Expenses;

import java.util.Scanner;

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
        System.out.println(Bold + Cyan + "Welcome To our Personal Budgeting Application\n" + Reset);
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
        System.out.println(Bold + Cyan + "Welcome To Income Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Display Expenses." + Reset);
        System.out.println(Bold + "2 -> Add Expense" + Reset);

        System.out.println(Bold + "3 -> Display Budgets" + Reset);
        System.out.println(Bold + "4 -> Add Budget." + Reset);

        System.out.println(Bold + "5 -> Add Income." + Reset);
        System.out.println(Bold + "6 -> Add Goal." + Reset);
        System.out.println(Bold + "7 -> Add Reminder." + Reset);
        System.out.println(Bold + Red + "8 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static int displayMainMenuPayment() {
        System.out.println(Bold + Cyan + "Welcome To Payment Section\n" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Display Transactions." + Reset);
        System.out.println(Bold + "2 -> Debt Repayment." + Reset);
        System.out.println(Bold + Red + "3 -> Add Donate." + Reset);
        System.out.println(Bold + Red + "4 -> Financial Reports." + Reset);
        System.out.println(Bold + Red + "5 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static void options(int choice, Budget bt, String fileName) {
        Scanner external_input = new Scanner(System.in);
        switch (choice) {

            case 1:
                boolean case1_valid = false;
                while (!case1_valid) {
                    try {
                        System.out.println(Bold + "Please enter your" + Green + " budget" + Reset + Bold + " amount: " + Reset);
                        double bud_amount2 = input.nextDouble();
                        bt.setBudget(bud_amount2);
                        bt.saveData(fileName);
                        case1_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.nextLine();
                    }
                }
                break;

            case 2:
                double am = 0;
                boolean case2_valid = false;
                while (!case2_valid) {
                    try {
                        System.out.println("How much did you pay: ");
                        am = external_input.nextDouble();
                        case2_valid = true;
                    } catch (Exception e) {
                        System.out.println(Bold + Red + "Invalid input, please try again" + Reset);
                        external_input.nextLine();
                    }
                }
                external_input.nextLine();
                System.out.println("What did you exactly pay for: ");
                String cat = external_input.nextLine();
                bt.addExpense(am, cat);
                bt.saveData(fileName);
                break;

            case 3:
                System.out.println("Your" + Bold + Red + " Expenses" + Reset + Bold + " are:" + Reset);

                int counter = 1;
                for (Expenses e : bt.expenses) {
                    System.out.println(Bold + counter + "- " + Red + e.amount + "$" + Reset + " for " + Bold + Blue + e.category + Reset);
                    counter++;
                }
                System.out.println("Which results in a total of: " + Bold + Red + bt.totalExpense + Reset + "\n");
                break;

            case 4:
                System.out.println("Your still have: " + Bold + Green + (bt.getBudget() - bt.totalExpense) + "$" + Reset);
                break;

            case 5:
                isContinue = false;
                break;

            case 6:
                isContinue = false;
                isMain = false;
                System.out.println(Purple + Bold + "Now I guess it's time to save some bytes, just like we save some" + Green + " cash" + Reset + Bold + Purple + ". Goodbye!" + Reset);
                break;

            default:
                System.out.println("Invalid option. Please choose a number between 1 and 6.");
        }
    }
}
