package com.example.techcorp;

public class GameEngine {

    private Company company;
    private ConsoleUI ui;
    private boolean running;
    private int turn;

    public GameEngine(Company company, ConsoleUI ui) {

        if (company == null) {
            throw new IllegalArgumentException(
                "Company cannot be null."
            );
        }

        if (ui == null) {
            throw new IllegalArgumentException(
                "ConsoleUI cannot be null."
            );
        }

        this.company = company;
        this.ui = ui;
        this.running = true;
        this.turn = 1;

        assert this.turn == 1 :
            "Initial turn should be 1.";

        assert this.running :
            "Game should start in running state.";
    }

    public void start() {

        while (running) {

             assert turn > 0 :
                "Turn number should always be positive.";

            ui.showTurnHeader(turn);
            ui.showCompanyStatus(company);
            ui.showMainMenu();

            int choice = ui.readMenuChoice();

            handleChoice(choice);

            if (running) {
                advanceTurn();
                turn++;
            }
        }
    }

    private void handleChoice(int choice) {

        switch (choice) {

            case 1 -> ui.showCompanyStatus(company);

            case 2 -> startPlannedProjects();

            case 3 -> workOnProjects();

            case 4 -> {
                running = false;
                ui.showMessage("Exiting game...");
            }

            default -> ui.showMessage("Invalid menu option.");
        }
    }

    private void startPlannedProjects() {

        for (Project project : company.getProjects()) {

            if (project.getStatus() == ProjectStatus.PLANNED) {
                project.start();
            }
        }

        ui.showMessage("All planned projects have been started.");
    }

    private void workOnProjects() {

        for (Project project : company.getProjects()) {
            project.workOneTurn();
        }

        try {
            
            company.paySalaries();

        } catch (IllegalStateException e) {
            ui.showMessage("Company cannot afford salaries.");
            ui.showMessage("Bankruptcy! Game over.");

            running = false;

            return;
        }
        
        ui.showMessage("Projects worked for one turn.");
    }

    private void advanceTurn() {

        if (allProjectsFinished()) {
            
            ui.showMessage("All projects are finished. Game over."
            );

            running = false;
        }
    }

    private boolean allProjectsFinished() {

        if (company.getProjects().isEmpty()) {
            return false;
        }

        for (Project project : company.getProjects()) {
            
            if (!project.isFinished()) {
                return false;
            }
        }

        return true;
    }
}