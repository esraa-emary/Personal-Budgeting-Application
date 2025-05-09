package org.example;

import static org.example.cliFormat.*;

import java.util.Scanner;

import static org.example.Menus.*;

public class Main {
    public static void main(String[] args) {


        while (isMain) {


            int choice = Menus.displayMainMenu();
            Scanner external_input = new Scanner(System.in);
            switch (choice) {

                case 1:
                    System.out.println("Now please enter the name of the file you would like to create with the proper extension: ");
                    String fileName = external_input.nextLine();
                    BudgetTracker bt = new BudgetTracker(fileName);
                    isContinue = true;
                    while (isContinue) {

                        int subChoice = Menus.dispalySubMenu();
                        Menus.options(subChoice, bt, fileName);
                    }
                    break;

                case 2:
                    System.out.println("Now enter the name of the file you would like to load with the proper extension: ");
                    String fileName2 = external_input.nextLine();
                    BudgetTracker bt2 = new BudgetTracker(fileName2);
                    isContinue = true;

                    while (isContinue) {

                        int subChoice2 = Menus.dispalySubMenu();
                        Menus.options(subChoice2, bt2, fileName2);
                    }
                    break;

                case 3:
                    System.out.println(Purple + Bold + "Now I guess it's time to save some bytes, just like we save some" + Green + " cash" + Reset + Bold + Purple + ". Goodbye!" + Reset);
                    isMain = false;
                    break;

                default:
                    System.out.println(Red + Bold + "Invalid option, please try again" + Reset);
                    break;
            }
        }
    }
}