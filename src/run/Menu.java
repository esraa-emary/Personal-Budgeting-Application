package run;

import static run.Format.*;

import income.Budget;
import income.Expenses;

import java.util.Scanner;

public class Menu {
    public static boolean isContinue = true;
    public static boolean isMain = true;
    public static Scanner input = new Scanner(System.in);

    public static int displayMainMenu() {
        System.out.println(Bold + Cyan + "Welcome to PennyPilot\n" + Reset);
        System.out.println(Bold + Cyan + ">>> " + Red + "Expenses" + Reset + Bold + " in check," + Green + " budget" + Reset + Bold + " on track" + Cyan + " <<<" + Reset);
        System.out.println(Bold + "Please choose an option from the menu below: \n" + Reset);
        System.out.println(Bold + "1 -> Create a new budget file" + Reset);
        System.out.println(Bold + "2 -> Load an existing budget file" + Reset);
        System.out.println(Bold + Red + "3 -> Exit" + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static int dispalySubMenu() {
        System.out.println("Now please choose an option from the menu below:");
        System.out.println(Bold + "1 -> Set your budget" + Reset);
        System.out.println(Bold + "2 -> Add Expense" + Reset);
        System.out.println(Bold + "3 -> View Expenses" + Reset);
        System.out.println(Bold + "4 -> View Remaining Budget" + Reset);
        System.out.println(Bold + "5 -> Return to main menu" + Reset);
        System.out.println(Bold + Red + "6 -> Exit\n" + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int subChoice = input.nextInt();
        input.nextLine();
        return subChoice;
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
