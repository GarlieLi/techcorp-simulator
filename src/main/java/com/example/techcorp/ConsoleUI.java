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
        System.out.println("2. Accept project");
        System.out.println("3. Put project on hold");
        System.out.println("4. Resume project");
        System.out.println("5. Expand team");
        System.out.println("6. End turn");
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

    public Difficulty chooseDifficulty() {
        System.out.println();
        System.out.println("=== AI DIFFICULTY ===");
        System.out.println("1. Easy");
        System.out.println("2. Normal");
        System.out.println("3. Hard");
        
        while (true) {
            System.out.print("Enter choice: ");
            
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                
                case 1:
                    return Difficulty.EASY;
                    
                case 2:
                    return Difficulty.NORMAL;
                    
                case 3:
                    return Difficulty.HARD;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
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

        System.out.println("0. Back");
        
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Invalid project selection.");
            return -1;
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        
        if (choice == 0) {
            return -1;
        }
        
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

    public void showExpandTeamMenu() {

        System.out.println();
        System.out.println("=== EXPAND TEAM ===");
        System.out.println();
        System.out.println("1. Hire Intern");
        System.out.println("   Hiring cost: 1000");
        System.out.println("   Salary per turn: 1000");
        System.out.println("   Productivity: 1");
        System.out.println();
        System.out.println("2. Hire FreelancerBot");
        System.out.println("   One-time cost: 18000");
        System.out.println("   Productivity: 4");
        System.out.println();
        System.out.println("3. Buy AutomatedTool");
        System.out.println("   One-time cost: 5000");
        System.out.println("   Productivity: 2");
        System.out.println();
        System.out.println("0. Back");
    }

    public void showTurnSummary(Company company) {
        
        if (company == null) {
            throw new IllegalArgumentException(
                "Company cannot be null."
            );
        }
        
        System.out.println(
            "Cash: "
            + formatAmount(company.getCash())
        );
        
        System.out.println(
            "Employees: "
            + company.getEmployees().size()
            + " (Salary: "
            + formatAmount(company.calculateTotalSalaries())
            + "/turn)"
        );
        
        System.out.println(
            "Productivity: "
            + company.calculateTotalProductivity()
        );
        
        System.out.println();
        System.out.println("Projects:");
        
        boolean hasActiveProjects = false;
        
        for (Project project : company.getProjects()) {
            
            if (project.getStatus() == ProjectStatus.FINISHED
                || project.getStatus() == ProjectStatus.CANCELLED) {
                continue;
            }
            hasActiveProjects = true;
            
            System.out.println(
                "- "
                + project.getName()
                + " ("
                + project.getStatus()
                + ") "
                + project.getProgress()
                + "/"
                + project.getRequiredWork()
            );
        }
        
        if (!hasActiveProjects) {
            System.out.println("None");
        }
        
        System.out.println();
    }

    public int chooseAvailableProject(Company company) {

        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null.");
        }

        if (company.getAvailableProjects().isEmpty()) {
            System.out.println("No available projects to accept.");
            return -1;
        }

        System.out.println();
        System.out.println("=== AVAILABLE PROJECTS ===");
        System.out.println();

        for (int i = 0; i < company.getAvailableProjects().size(); i++) {

            Project project = company.getAvailableProjects().get(i);

            System.out.println((i + 1) + ". " + project.getName());
            System.out.println("   Work: " + project.getRequiredWork());
            System.out.println("   Reward: " + formatAmount(project.getReward()));
            System.out.println();
        }

        System.out.println("0. Back");
        
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Invalid project selection.");
            return -1;
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        
        if (choice == 0) {
            return -1;
        }
        
        if (choice < 1 || choice > company.getAvailableProjects().size()) {
            System.out.println("Invalid project selection.");
            return -1;
        }
        
        return choice - 1;
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

        System.out.println();
        System.out.println("Employees: " + company.getEmployees().size());

        for (Employee employee : company.getEmployees()) {
            System.out.println(
                    "- " + employee.getName()
                    + " | role: " + employee.getRoleName()
                    + " | productivity: " + employee.work()
                    + " | salary: " + formatAmount(employee.getSalary())
            );
        }
        
        System.out.println("FreelancerBots: " + company.getFreelancerBots().size());
        System.out.println("AutomatedTools: " + company.getAutomatedTools().size());
        System.out.println();
        System.out.println("Projects Summary:");
        System.out.println("- Planned: " + company.countProjectsByStatus(ProjectStatus.PLANNED));
        System.out.println("- In Progress: " + company.countProjectsByStatus(ProjectStatus.IN_PROGRESS));
        System.out.println("- On Hold: " + company.countProjectsByStatus(ProjectStatus.ON_HOLD));
        System.out.println("- Finished: " + company.countProjectsByStatus(ProjectStatus.FINISHED));
        System.out.println();
        System.out.println("Current Active Projects:");

        if (company.getProjects().isEmpty()) {

            System.out.println("None");

        } else {

            boolean hasActiveProject = false;

            for (Project project : company.getProjects()) {

                if (project.getStatus() == ProjectStatus.FINISHED) {
                    continue;
                }

                hasActiveProject = true;

                System.out.println(
                        "- " + project.getName()
                        + " | progress: " + project.getProgress()
                        + "/" + project.getRequiredWork()
                        + " | reward: " + formatAmount(project.getReward())
                );
            }

            if (!hasActiveProject) {
                System.out.println("None");
            }
        }
    }

    public void showTurnResults(
            String label,
            double salaryPaid,
            double rewardsEarned,
            double remainingCash) {
            
        System.out.println(label);
        
        if (rewardsEarned > 0) {
            
            System.out.println(
                "+" + formatAmount(rewardsEarned)
                + " reward"
            );
        }
        
        System.out.println(
            "-" + formatAmount(salaryPaid)
            + " salaries"
        );
        
        System.out.println(
            "Cash: "
            + formatAmount(remainingCash)
        );
        
        System.out.println();
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

    public void showAiSummary(Company aiCompany) {
        
        if (aiCompany == null) {
            throw new IllegalArgumentException("Company cannot be null.");
        }
        
        System.out.println();
        System.out.println("=== AI COMPANY ===");
        System.out.println("Cash: " + formatAmount(aiCompany.getCash()));
        System.out.println("Projects: " + aiCompany.getProjects().size());
        System.out.println(
            "Finished: "
            + aiCompany.countProjectsByStatus(ProjectStatus.FINISHED)
        );
    }

    public void showGameOver(String outcome, Company player, Company ai) {

        if (outcome == null || outcome.isBlank()) {
            throw new IllegalArgumentException("Outcome cannot be null or blank.");
        }

        if (player == null || ai == null) {
            throw new IllegalArgumentException("Companies cannot be null.");
        }

        System.out.println();
        System.out.println("=== " + outcome + " ===");
        System.out.println();
        System.out.println("Player cash: " + formatAmount(player.getCash()));
        System.out.println(
                "Player finished projects: "
                + player.countProjectsByStatus(ProjectStatus.FINISHED)
        );
        System.out.println("AI cash: " + formatAmount(ai.getCash()));
        System.out.println(
                "AI finished projects: "
                + ai.countProjectsByStatus(ProjectStatus.FINISHED)
        );
    }
}