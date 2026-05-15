package com.example.techcorp;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void showTurnHeader(int turn) {
        assert turn > 0 : "Turn number should be positive.";
        System.out.println();
        System.out.println("=== TURN " + turn + " ===");
    }

    public void showMainMenu() {
        System.out.println("Choose an action:");
        System.out.println("1. Show company status");
        System.out.println("2. Start planned projects");
        System.out.println("3. Work on projects");
        System.out.println("4. Put project on hold");
        System.out.println("5. Resume project");
        System.out.println("6. Cancel project");
        System.out.println("7. Hire intern");
        System.out.println("8. Hire FreelancerBot");
        System.out.println("9. Buy AutomatedTool");
        System.out.println("0. Exit game");
    }

    public int readMenuChoice() {

        System.out.print("Enter choice: ");

        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        assert choice >= -1 : "Menu choice should not be less than -1.";

        return choice;
    }

    public int chooseProject(Company company) {

        if (company == null) {
            throw new IllegalArgumentException(
                    "Company cannot be null."
            );
        }

        if (company.getProjects().isEmpty()) {
            return -1;
        }

        System.out.println("Select project:");

        for (int i = 0; i < company.getProjects().size(); i++) {

            Project project = company.getProjects().get(i);

            System.out.println(
                    (i + 1)
                    + ". "
                    + project.getName()
                    + " ("
                    + project.getStatus()
                    + ")"
            );
        }

        System.out.print("Enter project number: ");

        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1
                || choice > company.getProjects().size()) {

            return -1;
        }

        return choice - 1;
    }

    public void showCompanyStatus(Company company) {
        
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null.");
        }

        System.out.println("Company: " + company.getName());
        System.out.println("Cash: " + company.getCash());
        System.out.println("Employees: "+ company.getEmployees().size());
        System.out.println("Projects:");

        if (company.getProjects().isEmpty()) {

            System.out.println("No projects available.");

        } else {
            
            for (Project project : company.getProjects()) {
                
                System.out.println(
                    "- " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | progress: " + project.getProgress()
                    + " / " + project.getRequiredWork()
                );
            }
        }
    }

    public void showMessage(String message) {

        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }

        System.out.println(message);
    }
}