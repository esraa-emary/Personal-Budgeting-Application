package run;

import java.util.Scanner;

/**
 * A simple greeting utility for the Personal Budgeting Application.
 * <p>
 * This class provides a basic greeting functionality that:
 * <ul>
 *   <li>Displays a welcome message</li>
 *   <li>Asks for the user's name</li>
 *   <li>Responds with a personalized greeting</li>
 * </ul>
 * <p>
 * It implements Runnable to be executed as a task.
 *
 * @author Budget Application Team
 * @version 1.0
 */
public class Greet implements Runnable {

    /**
     * Executes the greeting procedure.
     * <p>
     * This method:
     * <ol>
     *   <li>Outputs an initial greeting</li>
     *   <li>Prompts the user for their name</li>
     *   <li>Responds with a personalized greeting using the provided name</li>
     * </ol>
     */
    @Override
    public void run() {
        System.out.println("Hello, World!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name + "!");
    }
}