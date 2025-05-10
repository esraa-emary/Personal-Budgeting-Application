package run;

import static run.Format.*;

import income.Budget;
import income.Income;
import income.Expense;
import income.Goal;
import income.Reminder;

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
        System.out.println(Bold + "1 -> Display Transactions." + Reset);
        System.out.println(Bold + "2 -> Debt Repayment." + Reset);
        System.out.println(Bold + "3 -> Add Donate." + Reset);
        System.out.println(Bold + "4 -> Financial Reports." + Reset);
        System.out.println(Bold + Red + "5 -> Back." + Reset);
        System.out.printf(Bold + "choose an option: " + Reset);
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    public static void optionsIncome(int innerChoice, Budget bt, String fileName, Scanner external_input) {
        String source, date, title;
        double amount;
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
                System.out.println("Your still have from you budget: " + Bold + Green + (bt.getBudget() - bt.totalExpense) + "$" + Reset);
                System.out.println("You still have from you income: " + Bold + Green + (bt.totalIncome - bt.totalExpense) + "$" + Reset);
                break;

            case 7:
                System.out.println("Your" + Bold + Red + " Expenses" + Reset + Bold + " are:" + Reset);

                int counter = 1;
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
                System.out.print("Enter date to send reminder as (YYYY-MM-DD): ");
                date = external_input.next();

                boolean found = false;
                for (int remind = 0; remind < bt.reminders.size(); remind++) {
                    if (bt.reminders.get(remind).date.equals(date)) {
                        found = true;

                        String client_name = "ggggggggg";
                        String api_key_public = "998d404401aaaca06cea222204ee2179";
                        String api_key_private = "bae1a8be1b381bf1b52f31ea2e207d30";
                        String to_email = "esraaemary33@gmail.com";
                        String from_email = "assignmentsoftware16@gmail.com";
                        String subject = "Reminder for " + bt.reminders.get(remind).title + " on " + date;
                        String message_body = "Dear " + client_name + ", Your reminder is on " + date +
                                " for " + bt.reminders.get(remind).title;

                        String json_payload = String.format(
                                "{\"Messages\":[{\"From\":{\"Email\":\"%s\",\"Name\":\"Software Assignments\"},\"To\":[{\"Email\":\"%s\",\"Name\":\"%s\"}],\"Subject\":\"%s\",\"TextPart\":\"%s\"}]}",
                                from_email, to_email, to_email, subject, message_body
                        );

// Escape the JSON for command line
                        json_payload = json_payload.replace("\"", "\\\"");

                        List<String> command = new ArrayList<>();
                        command.add("curl");
                        command.add("-X");
                        command.add("POST");
                        command.add("https://api.mailjet.com/v3.1/send");
                        command.add("-H");
                        command.add("Content-Type: application/json");
                        command.add("-u");
                        command.add(api_key_public + ":" + api_key_private);
                        command.add("-d");
                        command.add(json_payload);  // Now properly escaped

                        System.out.println("\nSending reminder to " + to_email + "...");

                        try {
                            ProcessBuilder processBuilder = new ProcessBuilder(command);
                            processBuilder.redirectErrorStream(true);
                            Process process = processBuilder.start();

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream())
                            );

                            String line;
                            System.out.println("Mailjet response:");
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }

                            int exitCode = process.waitFor();
                            System.out.println("Exited with code: " + exitCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!found) {
                    System.out.println("There is no reminders.");
                }
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

    public static void optionsPayment(int innerChoice, Budget bt, String fileName, Scanner external_input) {
        switch (innerChoice) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
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
