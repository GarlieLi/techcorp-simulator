package com.example.techcorp;

public class GameEngine {

    private static final double INTERN_HIRE_COST = 2000;
    private static final double FREELANCER_BOT_COST = 4000;
    private static final double AUTOMATED_TOOL_COST = 3000;

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
            ui.showTurnSummary(company);
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

            case 2 -> acceptProject();

            case 3 -> workOnProject();

            case 4 -> putProjectsOnHold();

            case 5 -> resumeProjects();

            case 6 -> cancelProjects();

            case 7 -> expandTeam();

            case 0 -> {
                running = false;
                ui.showMessage("Exiting game...");
            }

            default -> ui.showMessage("Invalid menu option.");
        }
    }

    private void acceptProject() {

        int index = ui.chooseAvailableProject(company);

        if (index < 0) {
            return;
        }

        Project project = company.getAvailableProjects().get(index);
        company.acceptProject(project);

        ui.showMessage(project.getName() + " accepted and added to active projects.");
    }

    private void workOnProject() {

        int index = ui.chooseProject(
                company,
                "No active projects available to work on.",
                ProjectStatus.PLANNED,
                ProjectStatus.IN_PROGRESS
        );

        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);

        if (project.getStatus() == ProjectStatus.PLANNED) {
            project.start();
        }

        project.workOneTurn();

        try {

            company.paySalaries();
            company.collectProjectRewards();

        } catch (IllegalStateException e) {
            ui.showMessage("Company cannot afford salaries.");
            ui.showMessage("Bankruptcy! Game over.");

            running = false;

            return;
        }

        ui.showMessage(project.getName() + " worked for one turn.");
    }

    private void putProjectsOnHold() {

        int index = ui.chooseProject(
                company,
                "No in-progress projects available to put on hold.",
                ProjectStatus.IN_PROGRESS
        );

        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);
        project.putOnHold();
        ui.showMessage(project.getName() + " put on hold.");
    }

    private void resumeProjects() {

        int index = ui.chooseProject(
                company,
                "No projects available to resume.",
                ProjectStatus.ON_HOLD
        );

        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);
        project.resume();
        ui.showMessage(project.getName() + " resumed.");
    }

    private void cancelProjects() {

        int index = ui.chooseProject(
                company,
                "No projects available to cancel.",
                ProjectStatus.PLANNED,
                ProjectStatus.IN_PROGRESS,
                ProjectStatus.ON_HOLD
        );

        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);
        project.cancel();
        ui.showMessage(project.getName() + " cancelled.");
    }

    private void expandTeam() {

        while (true) {

            ui.showExpandTeamMenu();
            int choice = ui.readMenuChoice();

            switch (choice) {

                case 1 -> {
                    hireIntern();
                    return;
                }

                case 2 -> {
                    hireFreelancerBot();
                    return;
                }

                case 3 -> {
                    buyAutomatedTool();
                    return;
                }

                case 0 -> {
                    return;
                }

                default -> ui.showMessage("Invalid menu option.");
            }
        }
    }

    private void hireIntern() {

        if (!company.canAfford(INTERN_HIRE_COST)) {
            ui.showMessage("Not enough budget to hire intern.");
            return;
        }

        company.spendBudget(INTERN_HIRE_COST);
        Intern intern = new Intern(company.nextInternName(), 2, 1000);
        company.hire(intern);
        addWorkerToActiveProjects(intern);

        ui.showMessage("Intern hired successfully.");
    }

    private void hireFreelancerBot() {

        if (!company.canAfford(FREELANCER_BOT_COST)) {
            ui.showMessage("Not enough budget to hire FreelancerBot.");
            return;
        }

        company.spendBudget(FREELANCER_BOT_COST);
        FreelancerBot bot = new FreelancerBot(company.nextFreelancerBotName(), 5);
        company.addFreelancerBot(bot);
        addWorkerToActiveProjects(bot);

        ui.showMessage("FreelancerBot added to projects.");
    }

    private void buyAutomatedTool() {

        if (!company.canAfford(AUTOMATED_TOOL_COST)) {
            ui.showMessage("Not enough budget to buy AutomatedTool.");
            return;
        }

        company.spendBudget(AUTOMATED_TOOL_COST);
        AutomatedTool tool = new AutomatedTool(company.nextAutomatedToolName(), 3);
        company.addAutomatedTool(tool);
        addWorkerToActiveProjects(tool);

        ui.showMessage("AutomatedTool added to projects.");
    }

    private void addWorkerToActiveProjects(Workable worker) {

        for (Project project : company.getProjects()) {

            if (project.getStatus() != ProjectStatus.CANCELLED
                    && project.getStatus() != ProjectStatus.FINISHED) {
                project.addWorker(worker);
            }
        }
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

        if (!company.getAvailableProjects().isEmpty()) {
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