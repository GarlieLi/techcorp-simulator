package com.example.techcorp;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void showTurnHeader(int turn) {
        System.out.println();
        System.out.println("=== TURN " + turn + " ===");
    }

    public void showMainMenu() {
        System.out.println("Choose an action:");
        System.out.println("1. Show company status");
        System.out.println("2. Start planned projects");
        System.out.println("3. Work on projects");
        System.out.println("4. Exit game");
    }

    public int readMenuChoice() {
        System.out.print("Enter choice: ");

        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice;
    }

    public void showCompanyStatus(Company company) {
        System.out.println("Company: " + company.getName());
        System.out.println("Cash: " + company.getCash());
        System.out.println("Projects:");

        for (Project project : company.getProjects()) {
            System.out.println(
                "- " + project.getName()
                + " | status: " + project.getStatus()
                + " | progress: " + project.getProgress()
                + " / " + project.getRequiredWork()
            );
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}