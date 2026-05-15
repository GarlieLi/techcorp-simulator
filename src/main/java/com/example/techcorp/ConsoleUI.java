package com.example.techcorp;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("7. Hire intern (cost: 2000)");
        System.out.println("8. Hire FreelancerBot (cost: 4000)");
        System.out.println("9. Buy AutomatedTool (cost: 3000)");
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

        return chooseProject(company, "No projects available.");
    }

    public int chooseProject(
            Company company,
            String emptyMessage,
            ProjectStatus... allowedStatuses) {

        if (company == null) {
            throw new IllegalArgumentException(
                    "Company cannot be null."
            );
        }

        if (emptyMessage == null || emptyMessage.isBlank()) {
            throw new IllegalArgumentException(
                    "Empty message cannot be null or blank."
            );
        }

        List<Integer> matchingIndices = new ArrayList<>();

        for (int i = 0; i < company.getProjects().size(); i++) {

            Project project = company.getProjects().get(i);

            if (isAllowedStatus(project.getStatus(), allowedStatuses)) {
                matchingIndices.add(i);
            }
        }

        if (matchingIndices.isEmpty()) {
            System.out.println(emptyMessage);
            return -1;
        }

        System.out.println("Choose project:");

        for (int i = 0; i < matchingIndices.size(); i++) {

            Project project = company.getProjects().get(matchingIndices.get(i));

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
            System.out.println("Invalid project selection.");
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > matchingIndices.size()) {
            System.out.println("Invalid project selection.");
            return -1;
        }

        return matchingIndices.get(choice - 1);
    }

    private boolean isAllowedStatus(
            ProjectStatus status,
            ProjectStatus... allowedStatuses) {

        if (allowedStatuses == null || allowedStatuses.length == 0) {
            return true;
        }

        for (ProjectStatus allowed : allowedStatuses) {

            if (status == allowed) {
                return true;
            }
        }

        return false;
    }

    public void showTurnSummary(Company company) {

        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null.");
        }

        System.out.println("Company: " + company.getName());
        System.out.println("Cash: " + formatAmount(company.getCash()));
        System.out.println(
                "Employees: "
                + company.getEmployees().size()
                + " (Total salaries: "
                + formatAmount(company.calculateTotalSalaries())
                + ")"
        );
        System.out.println("Projects:");

        if (company.getProjects().isEmpty()) {

            System.out.println("No projects.");

        } else {

            for (Project project : company.getProjects()) {

                System.out.println(
                        "- " + project.getName()
                        + " (" + project.getStatus() + ") "
                        + project.getProgress()
                        + "/" + project.getRequiredWork()
                );
            }
        }
    }

    public void showCompanyStatus(Company company) {
        
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null.");
        }

        System.out.println();
        System.out.println("=== COMPANY STATUS ===");
        System.out.println("Company: " + company.getName());
        System.out.println("Cash: " + formatAmount(company.getCash()));
        System.out.println(
                "Total salaries: " + formatAmount(company.calculateTotalSalaries())
        );
        System.out.println("Employees (" + company.getEmployees().size() + "):");

        if (company.getEmployees().isEmpty()) {

            System.out.println("No employees.");

        } else {

            for (Employee employee : company.getEmployees()) {

                System.out.println(
                    "- " + employee.getName()
                    + " | role: " + employee.getRoleName()
                    + " | skill: " + employee.getSkill()
                    + " | salary: " + formatAmount(employee.getSalary())
                );
            }
        }

        System.out.println();
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

    private String formatAmount(double amount) {

        if (amount == (long) amount) {
            return String.valueOf((long) amount);
        }

        return String.valueOf(amount);
    }

    public void showMessage(String message) {

        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }

        System.out.println(message);
    }
}