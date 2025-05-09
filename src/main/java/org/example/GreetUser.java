package org.example;

import picocli.CommandLine;

import java.util.Scanner;

@CommandLine.Command(name = "greet", mixinStandardHelpOptions = true, version = "1.0", description = "A simple command that greets the user of our beloved CLI APP")
public class GreetUser implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello, World!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name + "!");
    }
}
