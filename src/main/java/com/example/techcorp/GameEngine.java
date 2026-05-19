package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private static final double INTERN_HIRE_COST = 1000;
    private static final double FREELANCER_BOT_COST = 8000;
    private static final double AUTOMATED_TOOL_COST = 5000;
    private static final double AI_MIN_CASH_TO_ACCEPT = 30000;

    private Company company;
    private Company aiCompany;
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
        this.aiCompany = initializeAiCompany(company);
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

            boolean turnAction = handleChoice(choice);

            if (running && turnAction) {
                
                processAiTurn();

                if (!resolveTurnEnd()) {
                    continue;
                }
            }
        }
    }

    private boolean handleChoice(int choice) {

        return switch (choice) {

            case 1 -> {
                ui.showCompanyStatus(company);
                yield false;
            }

            case 2 -> acceptProject();

            case 3 -> putProjectsOnHold();

            case 4 -> resumeProjects();

            case 5 -> cancelProjects();

            case 6 -> expandTeam();

            case 7 -> {
                ui.showMessage("Ending turn.");
                 yield true;
                }

            case 0 -> {
                running = false;
                ui.showMessage("Exiting game...");
                yield false;
            }

            default -> {
                ui.showMessage("Invalid menu option.");
                yield false;
            }
        };
    }

    private boolean resolveTurnEnd() {
        
        progressAllProjects(company);
        progressAllProjects(aiCompany);
        
        double playerSalary =
            company.calculateTotalSalaries();
            
        double playerCashBefore =
            company.getCash();
            
        try {
            
            company.paySalaries();
            company.collectProjectRewards();
        
        } catch (IllegalStateException e) {

        ui.showMessage(
            "Player cannot afford salaries. Bankruptcy!"
        );

        running = false;

        ui.showGameOver(
            "AI WINS",
            company,
            aiCompany
        );

        return false;
    }

    double playerRewards =
            company.getCash()
            - playerCashBefore
            + playerSalary;

    double aiSalary =
            aiCompany.calculateTotalSalaries();

    double aiCashBefore =
            aiCompany.getCash();

    try {

        aiCompany.paySalaries();
        aiCompany.collectProjectRewards();

    } catch (IllegalStateException e) {

        ui.showMessage(
            "AI company went bankrupt!"
        );

        running = false;

        ui.showGameOver(
            "PLAYER WINS",
            company,
            aiCompany
        );

        return false;
    }

    double aiRewards =
            aiCompany.getCash()
            - aiCashBefore
            + aiSalary;

    System.out.println();
    System.out.println("=== TURN RESULTS ===");
    System.out.println();

    ui.showTurnResults(
            "PLAYER",
            playerSalary,
            playerRewards,
            company.getCash()
    );

    ui.showTurnResults(
            "AI",
            aiSalary,
            aiRewards,
            aiCompany.getCash()
    );

    return true;
}

    private void progressAllProjects(Company targetCompany) {
        
        List<Project> activeProjects = new ArrayList<>();
        
        for (Project project : targetCompany.getProjects()) {
            
            if (project.getStatus() == ProjectStatus.PLANNED) {
                project.start();
            }
            
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                activeProjects.add(project);
            }
        }
        
        if (activeProjects.isEmpty()) {
            return;
        }

        String companyLabel =
            targetCompany == company
            ? "PLAYER"
            : "AI";
        
        int remainingProductivity =
            targetCompany.calculateTotalProductivity();
            
        List<Project> unfinishedProjects =
            new ArrayList<>(activeProjects);
            
        for (Project project : activeProjects) {
            
            int remainingWork =
                project.getRequiredWork()
                - project.getProgress();
                
            if (remainingWork <= remainingProductivity
                && unfinishedProjects.size() > 1) {

                project.workOneTurn(
                    remainingWork,
                    companyLabel
                );
                
                remainingProductivity -= remainingWork;
                
                unfinishedProjects.remove(project);
            }
        }
        
        if (unfinishedProjects.isEmpty()) {
            return;
        }
        
        int productivityPerProject =
            remainingProductivity
            / unfinishedProjects.size();
            
        if (productivityPerProject < 1) {
            productivityPerProject = 1;
        }
        
        for (Project project : unfinishedProjects) {

            project.workOneTurn(
                productivityPerProject,
                companyLabel
            );
        }
    }

    private boolean acceptProject() {

        int index = ui.chooseAvailableProject(company);

        if (index < 0) {
            return false;
        }

        Project project = company.getAvailableProjects().get(index);
        company.acceptProject(project);

        ui.showMessage(project.getName() + " accepted and added to active projects.");
        return true;
    }

    private boolean putProjectsOnHold() {

        int index = ui.chooseProject(
                company,
                "No in-progress projects available to put on hold.",
                ProjectStatus.IN_PROGRESS
        );

        if (index < 0) {
            return false;
        }

        Project project = company.getProjects().get(index);
        project.putOnHold();
        ui.showMessage(project.getName() + " put on hold.");
        return true;
    }

    private boolean resumeProjects() {

        int index = ui.chooseProject(
                company,
                "No projects available to resume.",
                ProjectStatus.ON_HOLD
        );

        if (index < 0) {
            return false;
        }

        Project project = company.getProjects().get(index);
        project.resume();
        ui.showMessage(project.getName() + " resumed.");
        return true;
    }

    private boolean cancelProjects() {

        int index = ui.chooseProject(
                company,
                "No projects available to cancel.",
                ProjectStatus.PLANNED,
                ProjectStatus.IN_PROGRESS,
                ProjectStatus.ON_HOLD
        );

        if (index < 0) {
            return false;
        }

        Project project = company.getProjects().get(index);
        project.cancel();
        ui.showMessage(project.getName() + " cancelled.");
        return true;
    }

    private boolean expandTeam() {

        while (true) {

            ui.showExpandTeamMenu();
            int choice = ui.readMenuChoice();

            switch (choice) {

                case 1 -> {
                    if (hireIntern()) {
                        return true;
                    }
                }

                case 2 -> {
                    if (hireFreelancerBot()) {
                        return true;
                    }
                }

                case 3 -> {
                    if (buyAutomatedTool()) {
                        return true;
                    }
                }

                case 0 -> {
                    return false;
                }

                default -> ui.showMessage("Invalid menu option.");
            }
        }
    }

    private boolean hireIntern() {

        if (!company.canAfford(INTERN_HIRE_COST)) {
            ui.showMessage("Not enough budget to hire intern.");
            return false;
        }

        company.spendBudget(INTERN_HIRE_COST);
        Intern intern = new Intern(company.nextInternName(), 2, 1000);
        company.hire(intern);
        addWorkerToCompanyProjects(company, intern);

        ui.showMessage("Intern hired successfully.");
        return true;
    }

    private boolean hireFreelancerBot() {

        if (!company.canAfford(FREELANCER_BOT_COST)) {
            ui.showMessage("Not enough budget to hire FreelancerBot.");
            return false;
        }

        company.spendBudget(FREELANCER_BOT_COST);
        FreelancerBot bot = new FreelancerBot(company.nextFreelancerBotName(), 5);
        company.addFreelancerBot(bot);
        addWorkerToCompanyProjects(company, bot);

        ui.showMessage("FreelancerBot added to projects.");
        return true;
    }

    private boolean buyAutomatedTool() {

        if (!company.canAfford(AUTOMATED_TOOL_COST)) {
            ui.showMessage("Not enough budget to buy AutomatedTool.");
            return false;
        }

        company.spendBudget(AUTOMATED_TOOL_COST);
        AutomatedTool tool = new AutomatedTool(company.nextAutomatedToolName(), 2);
        company.addAutomatedTool(tool);
        addWorkerToCompanyProjects(company, tool);

        ui.showMessage("AutomatedTool added to projects.");
        return true;
    }

    private void addWorkerToCompanyProjects(Company targetCompany, Workable worker) {

        for (Project project : targetCompany.getProjects()) {

            if (project.getStatus() != ProjectStatus.CANCELLED
                    && project.getStatus() != ProjectStatus.FINISHED) {
                project.addWorker(worker);
            }
        }
    }

    private Company initializeAiCompany(Company player) {
        
        Company ai = new Company("RivalTech", player.getCash());
        
        for (Employee employee : player.getEmployees()) {
            ai.hire(cloneEmployee(employee));
        }
        
        for (Project project : player.getAvailableProjects()) {
            ai.addAvailableProject(
                new Project(
                        project.getName(),
                        project.getRequiredWork(),
                        project.getReward()
                    )
                );
            }
            return ai;
        }
        
    private Employee cloneEmployee(Employee employee) {
        
        if (employee instanceof Developer) {
            return new Developer(
                employee.getName(),
                employee.getSkill(),
                employee.getSalary()
            );
        }
        
        if (employee instanceof Tester) {
            return new Tester(
                employee.getName(),
                employee.getSkill(),
                employee.getSalary()
            );
        }
        
        if (employee instanceof Manager) {
            return new Manager(
                employee.getName(),
                employee.getSkill(),
                employee.getSalary()
            );
        }
        
        if (employee instanceof Intern) {
            return new Intern(
                employee.getName(),
                employee.getSkill(),
                employee.getSalary()
            );
        }
        throw new IllegalStateException("Unknown employee type.");
    }
    
    private void processAiTurn() {
        
        int activeProjects = countActiveProjects(aiCompany);
        
        if (activeProjects == 0
                && aiCompany.getCash() > AI_MIN_CASH_TO_ACCEPT
                && !aiCompany.getAvailableProjects().isEmpty()) {
        
            Project project =
                aiCompany.getAvailableProjects().get(0);
            
            aiCompany.acceptProject(project);
            
            ui.showMessage(
                "AI accepted: " + project.getName()
            );
            
            return;
        }
        
        if (aiCompany.getCash() > 80000) {
            tryExpandAiTeam();
            return;
        }
        
        ui.showMessage("AI ends turn.");
    }

    private void tryExpandAiTeam() {

        if (aiCompany.getAutomatedTools().isEmpty()
                && aiCompany.getCash() > 45000
                && aiCompany.canAfford(AUTOMATED_TOOL_COST)) {
            aiCompany.spendBudget(AUTOMATED_TOOL_COST);
            AutomatedTool tool = new AutomatedTool(
                    aiCompany.nextAutomatedToolName(), 2);
            aiCompany.addAutomatedTool(tool);
            addWorkerToCompanyProjects(aiCompany, tool);
            return;
        }

        if (aiCompany.getFreelancerBots().isEmpty()
                && aiCompany.getCash() > 60000
                && aiCompany.canAfford(FREELANCER_BOT_COST)) {
            aiCompany.spendBudget(FREELANCER_BOT_COST);
            FreelancerBot bot = new FreelancerBot(
                    aiCompany.nextFreelancerBotName(), 5);
            aiCompany.addFreelancerBot(bot);
            addWorkerToCompanyProjects(aiCompany, bot);
            return;
        }

        if (aiCompany.getEmployees().size() < 4
                && aiCompany.getCash() > 35000
                && aiCompany.canAfford(INTERN_HIRE_COST)) {
            aiCompany.spendBudget(INTERN_HIRE_COST);
            Intern intern = new Intern(aiCompany.nextInternName(), 2, 1000);
            aiCompany.hire(intern);
            addWorkerToCompanyProjects(aiCompany, intern);
        }
    }

    private int countActiveProjects(Company targetCompany) {

        int count = 0;

        for (Project project : targetCompany.getProjects()) {

            if (project.getStatus() == ProjectStatus.PLANNED
                    || project.getStatus() == ProjectStatus.IN_PROGRESS
                    || project.getStatus() == ProjectStatus.ON_HOLD) {
                count++;
            }
        }

        return count;
    }
    
    private boolean hasActiveProject(Company company) {
        
        for (Project project : company.getProjects()) {
            
            if (project.getStatus() == ProjectStatus.PLANNED
                || project.getStatus() == ProjectStatus.IN_PROGRESS) {
                return true;
            }
        }
        
        return false;
    }

    private void advanceTurn() {

        boolean playerDone = hasCompletedAllProjects(company);
        boolean aiDone = hasCompletedAllProjects(aiCompany);

        if (!playerDone && !aiDone) {
            return;
        }

        running = false;

        if (playerDone && aiDone) {
            ui.showGameOver("DRAW", company, aiCompany);
        } else if (playerDone) {
            ui.showGameOver("PLAYER WINS", company, aiCompany);
        } else {
            ui.showGameOver("AI WINS", company, aiCompany);
        }
    }

    private boolean hasCompletedAllProjects(Company targetCompany) {

        if (targetCompany.getProjects().isEmpty()) {
            return false;
        }

        if (!targetCompany.getAvailableProjects().isEmpty()) {
            return false;
        }

        for (Project project : targetCompany.getProjects()) {

            if (!project.isFinished()) {
                return false;
            }
        }

        return true;
    }
}