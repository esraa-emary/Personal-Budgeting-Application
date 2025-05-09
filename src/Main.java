import static run.Format.*;
import static run.Menu.*;

import run.Menu;
import income.Budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (isMain) {
            int choice = Menu.displayMainMenu();
            Scanner external_input = new Scanner(System.in);
            switch (choice) {

                case 1:
                    System.out.println("Now please enter the name of the file you would like to create with the proper extension: ");
                    String fileName = external_input.nextLine();
                    Budget bt = new Budget(fileName);
                    isContinue = true;
                    while (isContinue) {
                        int subChoice = Menu.dispalySubMenu();
                        Menu.options(subChoice, bt, fileName);
                    }
                    break;

                case 2:
                    System.out.println("Now enter the name of the file you would like to load with the proper extension: ");
                    String fileName2 = external_input.nextLine();
                    Budget bt2 = new Budget(fileName2);
                    isContinue = true;

                    while (isContinue) {
                        int subChoice2 = Menu.dispalySubMenu();
                        Menu.options(subChoice2, bt2, fileName2);
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