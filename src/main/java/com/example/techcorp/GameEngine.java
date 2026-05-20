package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private static final double INTERN_HIRE_COST = 1000;
    private static final double FREELANCER_BOT_COST = 18000;
    private static final double AUTOMATED_TOOL_COST = 5000;
    private static final double AI_MIN_CASH_TO_ACCEPT = 30000;

    private Company company;
    private Company aiCompany;
    private ConsoleUI ui;
    private boolean running;
    private int turn;
    private Difficulty difficulty;
    private double playerExpansionCost;
    private double aiExpansionCost;
    
    public GameEngine(
        Company company,
        ConsoleUI ui,
        Difficulty difficulty){

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

        if (difficulty == null) {
            throw new IllegalArgumentException(
                "Difficulty cannot be null."
            );
        }

        this.company = company;
        this.aiCompany = initializeAiCompany(company);
        this.ui = ui;
        this.difficulty = difficulty;
        this.running = true;
        this.turn = 1;

        assert this.turn == 1 :
            "Initial turn should be 1.";

        assert this.running :
            "Game should start in running state.";
    }

    public void start() {
        
        while (running) {

            playerExpansionCost = 0;
            aiExpansionCost = 0;
            
            ui.showTurnHeader(turn, true);
            boolean turnEnded = false;
            boolean firstMenu = true;
            
            while (running && !turnEnded) {
                if (!firstMenu) {
                    ui.showTurnHeader(turn, false);
                }
                ui.showTurnSummary(company);

                ui.showMainMenu();
                int choice = ui.readMenuChoice();
                turnEnded = handleChoice(choice);
                firstMenu = false;
            }
            if (!running) {
                break;
            }
            
            List<String> aiActions = processAiTurn();
            
            if (!resolveTurnEnd(aiActions)) {
                continue;
            }
            
            advanceTurn();

            if (!running) {
                break;
            }
            turn++;
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

            case 5 -> {expandTeam();
                yield false;
            }

            case 6 -> {
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

    private boolean resolveTurnEnd(List<String> aiActions) {
        
        List<String> playerProjectSummary = progressAllProjects(company);
        List<String> aiProjectSummary = progressAllProjects(aiCompany);
        
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

    double playerNetIncome =
            playerRewards
            - playerSalary
            - playerExpansionCost;

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

    double aiNetIncome =
            aiRewards
            - aiSalary
            - aiExpansionCost;

    System.out.println();
    System.out.println(
        "=== TURN "
        + turn
        + " SUMMARY ==="
    );

    System.out.println();

    System.out.println("AI Actions:");

    if (aiActions.isEmpty()) {

        System.out.println("- No actions");

    } else {

        for (String action : aiActions) {

            System.out.println(
                "- " + action
            );
        }
    }

    System.out.println();

    System.out.println("PLAYER");
    System.out.println("Projects:");

    for (String line : playerProjectSummary) {

        System.out.println(line);
    }

    System.out.println();

    System.out.println("Financial:");
    
    if (playerRewards > 0) {
        
        System.out.println(
            "Reward: +"
            + (long) playerRewards
        );
    }
    
    System.out.println(
        "Salaries: -"
        + (long) playerSalary
    );
    
    if (playerExpansionCost > 0) {
        
        System.out.println(
            "Expansion: -"
            + (long) playerExpansionCost
        );
    }
    
    System.out.println(
        "Net Income: "
        + (playerNetIncome >= 0 ? "+" : "")
        + (long) playerNetIncome
    );
    
    System.out.println(
        "Cash: "
        + (long) company.getCash()
    );
    
    System.out.println();
    System.out.println("Company:");
    
    System.out.println(
        "Employees: "
        + company.getEmployees().size()
        + " | FreelancerBots: "
        + company.getFreelancerBots().size()
        + " | AutomatedTools: "
        + company.getAutomatedTools().size()
    );
    
    System.out.println(
        "Productivity: "
        + company.calculateTotalProductivity()
    );

    System.out.println();

    System.out.println("AI");
    System.out.println("Projects:");

    for (String line : aiProjectSummary) {

        System.out.println(line);
    }

    System.out.println();

    System.out.println("Financial:");
    
    if (aiRewards > 0) {
        System.out.println(
            "Reward: +"
            + (long) aiRewards
        );
    }
    
    System.out.println(
        "Salaries: -"
        + (long) aiSalary
    );
    
    if (aiExpansionCost > 0) {
        System.out.println(
            "Expansion: -"
            + (long) aiExpansionCost
        );
    }
    
    System.out.println(
        "Net Income: "
        + (aiNetIncome >= 0 ? "+" : "")
        + (long) aiNetIncome
    );
    
    System.out.println(
        "Cash: "
        + (long) aiCompany.getCash()
    );
    
    System.out.println();
    System.out.println("Company:");
    
    System.out.println(
        "Employees: "
        + aiCompany.getEmployees().size()
        + " | FreelancerBots: "
        + aiCompany.getFreelancerBots().size()
        + " | AutomatedTools: "
        + aiCompany.getAutomatedTools().size()
    );
    
    System.out.println(
        "Productivity: "
        + aiCompany.calculateTotalProductivity()
    );

    System.out.println();
    return true;
    }

    private List<String> progressAllProjects(Company targetCompany){

        List<String> summary = new ArrayList<>();
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
            return summary;
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
                    remainingWork
                );
                
                remainingProductivity -= remainingWork;
                
                unfinishedProjects.remove(project);
            }
        }
        
        if (!unfinishedProjects.isEmpty()) {
            
            int productivityPerProject =
                remainingProductivity
                / unfinishedProjects.size();
                
            if (productivityPerProject < 1) {
                productivityPerProject = 1;
            }
        }
        
        for (Project project : unfinishedProjects) {
            int productivityPerProject =
                remainingProductivity
                / unfinishedProjects.size();
            
            if (productivityPerProject < 1) {
                productivityPerProject = 1;
            }
            
            project.workOneTurn(
                productivityPerProject
            );

            if (project.isFinished()) {
                summary.add(
                    "- "
                    + project.getName()
                    + " COMPLETED (+"
                    + (long) project.getReward()
                    + ")"
                );
            } else {
                
                summary.add(
                    "- "
                    + project.getName()
                    + ": "
                    + project.getProgress()
                    + "/"
                    + project.getRequiredWork()
                );
            }
        }
        return summary;
    }

    private boolean acceptProject() {

        int index = ui.chooseAvailableProject(company);

        if (index < 0) {
            return false;
        }

        Project project = company.getAvailableProjects().get(index);
        company.acceptProject(project);

        ui.showMessage(project.getName() + " accepted and added to active projects.");
        return false;
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
        return false;
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
        return false;
    }

    private boolean expandTeam() {
        
        while (true) {
            ui.showExpandTeamMenu();
            int choice = ui.readMenuChoice();
            
            switch (choice) {
                case 1 -> {
                    hireIntern();
                    return true;
                }
                
                case 2 -> {
                    hireFreelancerBot();
                    return true;
                }
                
                case 3 -> {
                    buyAutomatedTool();
                    return true;
                }
                
                case 0 -> {
                    return false;
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
        playerExpansionCost += INTERN_HIRE_COST;
        Intern intern = new Intern(company.nextInternName(), 2, 1000);
        company.hire(intern);
        addWorkerToCompanyProjects(company, intern);

        ui.showMessage("Intern hired successfully.");
        return;
    }

    private void hireFreelancerBot() {

        if (!company.canAfford(FREELANCER_BOT_COST)) {
            ui.showMessage("Not enough budget to hire FreelancerBot.");
            return;
        }

        company.spendBudget(FREELANCER_BOT_COST);
        playerExpansionCost += FREELANCER_BOT_COST;
        FreelancerBot bot = new FreelancerBot(company.nextFreelancerBotName(), 4);
        company.addFreelancerBot(bot);
        addWorkerToCompanyProjects(company, bot);

        ui.showMessage("FreelancerBot added to projects.");
        return;
    }

    private void buyAutomatedTool() {

        if (!company.canAfford(AUTOMATED_TOOL_COST)) {
            ui.showMessage("Not enough budget to buy AutomatedTool.");
            return;
        }

        company.spendBudget(AUTOMATED_TOOL_COST);
        playerExpansionCost += AUTOMATED_TOOL_COST;
        AutomatedTool tool = new AutomatedTool(company.nextAutomatedToolName(), 2);
        company.addAutomatedTool(tool);
        addWorkerToCompanyProjects(company, tool);

        ui.showMessage("AutomatedTool added to projects.");
        return;
    }

    private void addWorkerToCompanyProjects(Company targetCompany, Workable worker) {

        for (Project project : targetCompany.getProjects()) {

            if (project.getStatus() != ProjectStatus.FINISHED) {
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

    private List<String> processAiTurn() {

        List<String> aiActions = new ArrayList<>();
        
        int actions = 0;
        
        int maxActions;
        
        switch (difficulty) {
            case EASY:
                maxActions = 1;
            break;
                
            case HARD:
                maxActions = 3;
            break;
                
            default:
                maxActions = 2;
            }

        double aiSafetyCash;
        
        switch (difficulty) {
            case EASY:
                aiSafetyCash = 45000;
            break;
                
            case HARD:
                aiSafetyCash = 20000;
            break;
                
            default:
                aiSafetyCash = 30000;
            }
            
        while (actions < maxActions){

            if (aiCompany.getCash()
                    < aiCompany.calculateTotalSalaries() * 2) {
                break;
            }
            
            boolean actionTaken = false;
            
            int activeProjects =
                countActiveProjects(aiCompany);
                
            int productivity =
                aiCompany.calculateTotalProductivity();
                
            if (activeProjects == 0
                && !aiCompany.getAvailableProjects().isEmpty()) {
                
                Project bestProject =
                    chooseBestProject(aiCompany);
                    
                aiCompany.acceptProject(bestProject);
                
                aiActions.add(
                    "Accepted project: "
                    + bestProject.getName()
                );
                actionTaken = true;
            }

            activeProjects = countActiveProjects(aiCompany);
            productivity = aiCompany.calculateTotalProductivity();
            
            if (aiCompany.getCash() > 35000
                && productivity < 50
                && aiCompany.calculateTotalSalaries()
                    < aiCompany.getCash() / 2) {
                
                actionTaken = tryExpandAiTeam(
                    aiActions,
                    aiSafetyCash
                );
            }

            activeProjects = countActiveProjects(aiCompany);
            productivity = aiCompany.calculateTotalProductivity();

            if (
                (difficulty == Difficulty.EASY
                    && activeProjects < Math.max(1, productivity / 18)
                )
                ||
                (difficulty == Difficulty.NORMAL
                    && activeProjects < Math.max(1, productivity / 16)
                )
                ||
                (difficulty == Difficulty.HARD
                    && activeProjects < Math.max(1, productivity / 12)
                )
            ) {
                
                if (!aiCompany.getAvailableProjects().isEmpty()
                    && aiCompany.getCash() > aiSafetyCash) {
                
                    Project bestProject = chooseBestProject(aiCompany);
                    
                    aiCompany.acceptProject(bestProject);
                    
                    aiActions.add(
                        "Accepted project: "
                        + bestProject.getName()
                    );
                    
                    actionTaken = true;
                }
            }
            
            if (!actionTaken) {
                break;
            }
            actions++;
        }
        return aiActions;
    }

    private Project chooseBestProject(Company targetCompany) {
        
        Project bestProject = null;
        double bestValue = 0;
        
        int productivity =
            targetCompany.calculateTotalProductivity();
            
        for (Project project : targetCompany.getAvailableProjects()) {

            if (difficulty != Difficulty.HARD
                && turn <= 2
                && project.getRequiredWork() > 60){
                continue;
            }
            
            if (project.getRequiredWork() > productivity * 3) {
                continue;
            }
            
            double value =
                (double) project.getReward()
                / project.getRequiredWork();
                
            if (value > bestValue) {
                
                bestValue = value;
                bestProject = project;
            }
        }
        
        if (bestProject == null
            && !targetCompany.getAvailableProjects().isEmpty()) {
            
            bestProject =
            targetCompany.getAvailableProjects().get(0);
        }
        return bestProject;
    }

    private boolean tryExpandAiTeam(
        List<String> aiActions,
        double aiSafetyCash){

        int maxTools;
        
        switch (difficulty) {
            case EASY:
                maxTools = 1;
            break;
                
            case HARD:
                maxTools = 3;
            break;
            
            default:
                maxTools = 2;
            }

        if (aiCompany.getAutomatedTools().size() < maxTools
                && aiCompany.getCash() > aiSafetyCash + 15000
                && aiCompany.canAfford(AUTOMATED_TOOL_COST)
                && aiCompany.getCash() - AUTOMATED_TOOL_COST >= 30000) {

            aiCompany.spendBudget(AUTOMATED_TOOL_COST);
            aiExpansionCost += AUTOMATED_TOOL_COST;

            AutomatedTool tool = new AutomatedTool(
                    aiCompany.nextAutomatedToolName(), 2);

            aiCompany.addAutomatedTool(tool);
            addWorkerToCompanyProjects(aiCompany, tool);

            aiActions.add("Bought AutomatedTool");
            return true;
        }

        int maxBots;
        
        switch (difficulty) {
            case EASY:
                maxBots = 1;
            break;
            
            case HARD:
                maxBots = 3;
            break;
            
            default:
                maxBots = 2;
            }

        if (aiCompany.getFreelancerBots().size() < maxBots
                && aiCompany.getCash() > aiSafetyCash + 40000
                && aiCompany.canAfford(FREELANCER_BOT_COST)
                && aiCompany.getCash() - FREELANCER_BOT_COST >= 30000) {

            aiCompany.spendBudget(FREELANCER_BOT_COST);
            aiExpansionCost += FREELANCER_BOT_COST;

            FreelancerBot bot = new FreelancerBot(
                    aiCompany.nextFreelancerBotName(), 4);

            aiCompany.addFreelancerBot(bot);
            addWorkerToCompanyProjects(aiCompany, bot);

            aiActions.add("Hired FreelancerBot");
            return true;
        }

        if (aiCompany.getEmployees().size() < 5
                && aiCompany.getCash() > aiSafetyCash + 10000
                && aiCompany.canAfford(INTERN_HIRE_COST)) {
                    
            aiCompany.spendBudget(INTERN_HIRE_COST);
            aiExpansionCost += INTERN_HIRE_COST;
            Intern intern = new Intern(aiCompany.nextInternName(), 2, 1000);

            aiCompany.hire(intern);

            aiActions.add("Hired Intern");
            addWorkerToCompanyProjects(aiCompany, intern);
            return true;
        }
        return false;
    }

    private int countActiveProjects(Company targetCompany) {

        int count = 0;

        for (Project project : targetCompany.getProjects()) {

            if (project.getStatus() == ProjectStatus.PLANNED
                    || project.getStatus() == ProjectStatus.IN_PROGRESS) {
                count++;
            }
        }

        return count;
    }

    private void advanceTurn() {

        boolean playerDone = hasCompletedAllProjects(company);
        boolean aiDone = hasCompletedAllProjects(aiCompany);

        if (!playerDone && !aiDone) {
            return;
        }

        running = false;

        if (playerDone && aiDone) {

            if (company.getCash() > aiCompany.getCash()) {
                ui.showGameOver(
                    "PLAYER WINS",
                    company,
                    aiCompany
                );
            } else if (aiCompany.getCash() > company.getCash()) {
                ui.showGameOver(
                    "AI WINS",
                    company,
                    aiCompany
                );
            } else {
                ui.showGameOver(
                    "DRAW",
                    company,
                    aiCompany
                );
            }

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