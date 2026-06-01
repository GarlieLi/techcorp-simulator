package com.example.techcorp;

import java.util.List;
import java.util.ArrayList;

/**
 * Service layer for the web version of TechCorp Simulator.
 * Handles game state, player actions, AI actions,
 * turn progression, and game results.
 */

public class GameService {

    private Company company;
    private Company aiCompany;

    private int turn;

    private String turnLog;
    private String aiDifficulty;
    private String winner;

    private boolean gameStarted;
    private boolean gameOver;

    private double playerExpansionCost;
    private double aiExpansionCost;

    public GameService() {

        company = new Company(
            "TechCorp",
            50000
        );

        turn = 1;

        aiDifficulty = "NORMAL";
        gameStarted = false;
        gameOver = false;
        winner = "";

        turnLog = "Game started.";

        company.hire(
            new Developer(
                "Anna",
                10,
                6000
            )
        );

        company.hire(
            new Tester(
                "Piotr",
                6,
                4000
            )
        );

        company.hire(
            new Manager(
                "Ewa",
                5,
                5000
            )
        );

        aiCompany = new Company(
            "AI Corp",
            50000
        );
        
        aiCompany.hire(
            new Developer(
                "AI Dev",
                10,
                6000
            )
        );
        
        aiCompany.hire(
            new Tester(
                "AI Tester",
                6,
                4000
            )
        );
        
        aiCompany.hire(
            new Manager(
                "AI Manager",
                5,
                5000
            )
        );

        addProjects(company);
        
        addProjects(aiCompany);
    }

// Creates and adds all available projects to a company at game startup.

        private void addProjects(
            Company target) {
                
            target.addAvailableProject(
                new Project(
                    "Bug Fix Sprint",
                    15,
                    18000
                )
            );
            
            target.addAvailableProject(
                new Project(
                    "Website",
                    20,
                    28000
                )
            );
            
            target.addAvailableProject(
                new Project(
                    "Mobile App",
                    30,
                    45000
                )
            );
            
            target.addAvailableProject(
                new Project(
                    "AI Chatbot",
                    60,
                    90000
                )
            );
            
            target.addAvailableProject(
                new Project(
                    "Cybersecurity Audit",
                    75,
                    110000
                )
            );
            
            target.addAvailableProject(
                new Project(
                    "Cloud Infrastructure",
                    110,
                    150000
                )
            );
        }

    public Company getCompany() {

        return company;
    }

    public int getTurn() {
        return turn;
    }

// Accepts a project selected by the player.

    public String acceptProject(
        String projectName) {
            
        for (Project project
            : company.getAvailableProjects()) {
                
            if (project.getName()
                .equalsIgnoreCase(
                    projectName)) {
                        
                company.acceptProject(project);
                
                return project.getName()
                    + " accepted.";
            }
        }
        return "Project not found.";
    }

// Ends the current turn and processes
// project progress, salaries, rewards, and AI actions.

    public String endTurn() {

        playerExpansionCost = 0;
        aiExpansionCost = 0;
        
        StringBuilder log =
            new StringBuilder();
        
        progressAllProjects(company);
        
        try {
            company.paySalaries();

        } catch (IllegalStateException e) {
            
            gameOver = true;
            winner = "AI WINS";
            turnLog = "PLAYER BANKRUPT";

            return "Game Over";
        }

        company.collectProjectRewards();
        
        processAiTurn(log);

        progressAllProjects(aiCompany);
        
        try {
            aiCompany.paySalaries();
        } catch (IllegalStateException e) {
            
            gameOver = true;
            winner = "PLAYER WINS";
            turnLog = "AI BANKRUPT";
            return "Game Over";
        }

        aiCompany.collectProjectRewards();

        advanceTurn();
        
        turn++;
        
        turnLog = log.toString();
        
        return "Turn ended.";
    }

// Player team expansion actions.

    public String hireIntern() {

        if (!company.canAfford(1000)) {
            return "Not enough cash.";
        }

        company.spendBudget(1000);

        Intern intern =
        new Intern(
            company.nextInternName(),
            2,
            1000
        );

        company.hire(intern);

        playerExpansionCost += 1000;

        addWorkerToCompanyProjects(
            company,
            intern
        );

        return intern.getName()
        + " hired.";
    }

    public String hireFreelancerBot() {
        
        if (!company.canAfford(18000)) {
            return "Not enough cash.";
        }
        
        company.spendBudget(18000);
        
        FreelancerBot bot =
            new FreelancerBot(
            company.nextFreelancerBotName(),
            4
        );
        
        company.addFreelancerBot(bot);

        playerExpansionCost += 18000;
        
        addWorkerToCompanyProjects(
            company,
            bot
        );
        
        return bot.getName()
        + " hired.";
    }

    public String buyAutomatedTool() {
        
        if (!company.canAfford(5000)) {
            return "Not enough cash.";
        }
        
        company.spendBudget(5000);
        
        AutomatedTool tool =
            new AutomatedTool(
            company.nextAutomatedToolName(),
            2
        );
        
        company.addAutomatedTool(tool);

        playerExpansionCost += 5000;
        
        addWorkerToCompanyProjects(
            company,
            tool
        );
        
        return tool.getName()
            + " purchased.";
    }

// Adds a newly hired worker to all active projects.

    private void addWorkerToCompanyProjects(
            Company targetCompany,
            Workable worker) {
                
        for (Project project
                : targetCompany.getProjects()) {
                    
            if (project.getStatus()
                    != ProjectStatus.FINISHED) {

                project.addWorker(worker);
            }
        }
    }

// Project management actions.

    public String holdProject(String projectName) {
        
        for (Project project : company.getProjects()) {
            
            if (project.getName().equals(projectName)) {
                
                project.putOnHold();
                
                return projectName
                + " put on hold.";
            }
        }
        return "Project not found.";
    }

    public String resumeProject(String projectName) {
        
        for (Project project : company.getProjects()) {
            
            if (project.getName().equals(projectName)) {
                
                project.resume();
                
                return projectName
                + " resumed.";
            }
        }
        return "Project not found.";
    }

    public String cancelProject(
            String projectName) {
                
        for (Project project
                : company.getProjects()) {
                    
            if (
                project.getName()
                    .equals(projectName)
                &&
                project.getStatus()
                    == ProjectStatus.PLANNED
            ) {

                company.getProjects()
                    .remove(project);

                company
                    .getAvailableProjects()
                    .add(project);

                return projectName
                    + " cancelled.";
            }
        }
        return "Project cannot be cancelled.";
    }

    public Company getAiCompany() {
        return aiCompany;
    }

// Main AI decision-making process.
// Controls project selection and company expansion.

    private void processAiTurn(
            StringBuilder log) {
                
        int actions = 0;
        
        int maxActions;
        
        switch (aiDifficulty) {
            
            case "EASY":
                maxActions = 1;
                break;
                
            case "HARD":
                maxActions = 3;
                break;
                
            default:
                maxActions = 2;
        }
            
        double aiSafetyCash;
        
        switch (aiDifficulty) {
            
            case "EASY":
                aiSafetyCash = 45000;
                break;
                
            case "HARD":
                aiSafetyCash = 20000;
                break;
                
            default:
                aiSafetyCash = 30000;
        }
        
        while (actions < maxActions) {
            
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
                && !aiCompany
                    .getAvailableProjects()
                    .isEmpty()) {

                Project bestProject =
                    chooseBestProject(aiCompany);

                aiCompany.acceptProject(bestProject);

                log.append(
                    "AI accepted "
                    + bestProject.getName()
                    + "<br>"
                );
                
                actionTaken = true;
            }
            
            activeProjects =
                countActiveProjects(aiCompany);
                
            productivity =
                aiCompany.calculateTotalProductivity();

            if (aiCompany.getCash() > 35000
                    && productivity < 50
                    && aiCompany.calculateTotalSalaries()
                        < aiCompany.getCash() / 2) {
                            
                actionTaken =
                    tryExpandAiTeam(
                        log,
                        aiSafetyCash
                    );
            }
            
            activeProjects =
                countActiveProjects(aiCompany);
                
            productivity =
                aiCompany.calculateTotalProductivity();

            boolean shouldAcceptProjects;

            if (aiDifficulty.equals("EASY")) {

                shouldAcceptProjects =
                    activeProjects
                    < Math.max(1,
                        productivity / 18);

            } else if (aiDifficulty.equals("HARD")) {

                shouldAcceptProjects =
                    activeProjects
                    < Math.max(1,
                        productivity / 12);

            } else {

                shouldAcceptProjects =
                    activeProjects
                    < Math.max(1,
                        productivity / 16);
            }
            
            if (shouldAcceptProjects
                    &&
                !aiCompany
                    .getAvailableProjects()
                    .isEmpty()
                    &&
                aiCompany.getCash()
                    > aiSafetyCash) {

                Project bestProject =
                    chooseBestProject(aiCompany);

                aiCompany.acceptProject(bestProject);

                log.append(
                    "AI accepted "
                    + bestProject.getName()
                    + "<br>"
                );
                actionTaken = true;
            }
            
            if (!actionTaken) {
                break;
            }
            actions++;
        }
    }

// Chooses the most profitable project
// available for the AI company.

    private Project chooseBestProject(
            Company targetCompany) {

        Project bestProject = null;

        double bestValue = 0;

        int productivity =
            targetCompany
                .calculateTotalProductivity();

        for (Project project
                : targetCompany
                  .getAvailableProjects()) {

            if (!aiDifficulty.equals("HARD")
                    &&
                turn <= 2
                    &&
                project.getRequiredWork() > 60) {

                continue;
            }

            if (project.getRequiredWork()
                    > productivity * 3) {

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
                &&
            !targetCompany
                .getAvailableProjects()
                .isEmpty()) {

            bestProject =
                targetCompany
                    .getAvailableProjects()
                    .get(0);
        }
        return bestProject;
    }

// Counts active projects for workload planning.

    public int countActiveProjects(
            Company targetCompany) {

        int count = 0;

        for (Project project
                : targetCompany.getProjects()) {

            if (project.getStatus()
                    == ProjectStatus.PLANNED
                ||
                project.getStatus()
                    == ProjectStatus.IN_PROGRESS) {

                count++;
            }
        }
        return count;
    }

// AI hiring and expansion strategy

    private boolean tryExpandAiTeam(
            StringBuilder log,
            double aiSafetyCash) {
                
        int maxTools;

        switch (aiDifficulty) {

            case "EASY":
                maxTools = 1;
                break;

            case "HARD":
                maxTools = 3;
                break;

            default:
                maxTools = 2;
        }
        
        if (aiCompany.getAutomatedTools().size()
                < maxTools
                &&
            aiCompany.getCash()
                > aiSafetyCash + 15000
                &&
            aiCompany.canAfford(5000)
                &&
            aiCompany.getCash() - 5000
                >= 30000) {
                    
            aiCompany.spendBudget(5000);
            
            AutomatedTool tool =
                new AutomatedTool(
                    aiCompany.nextAutomatedToolName(),
                    2
                );

            aiCompany.addAutomatedTool(tool);

            aiExpansionCost += 5000;

            addWorkerToCompanyProjects(
                aiCompany,
                tool
            );

            log.append(
                "AI bought AutomatedTool<br>"
            );

            return true;
        }

        int maxBots;

        switch (aiDifficulty) {

            case "EASY":
                maxBots = 1;
                break;

            case "HARD":
                maxBots = 3;
                break;

            default:
                maxBots = 2;
        }
            
        if (aiCompany.getFreelancerBots().size()
                < maxBots
                &&
            aiCompany.getCash()
                > aiSafetyCash + 40000
                &&
            aiCompany.canAfford(18000)
                &&
            aiCompany.getCash() - 18000
                >= 30000) {

            aiCompany.spendBudget(18000);

            FreelancerBot bot =
                new FreelancerBot(
                    aiCompany.nextFreelancerBotName(),
                    4
                );

            aiCompany.addFreelancerBot(bot);

            aiExpansionCost += 18000;
            
            addWorkerToCompanyProjects(
                aiCompany,
                bot
            );

            log.append(
                "AI hired FreelancerBot<br>"
            );
            
            return true;
        }
        
        if (aiCompany.getEmployees().size()
                < 5
                &&
            aiCompany.getCash()
                > aiSafetyCash + 10000
                &&
            aiCompany.canAfford(1000)) {

            aiCompany.spendBudget(1000);

            Intern intern =
                new Intern(
                    aiCompany.nextInternName(),
                    2,
                    1000
                );

            aiCompany.hire(intern);

            aiExpansionCost += 1000;
            
            addWorkerToCompanyProjects(
                aiCompany,
                intern
            );

            log.append(
                "AI hired Intern<br>"
            );
            
            return true;
        }
        return false;
    }

// Sets AI difficulty before the game starts.

    public void setAiDifficulty(
            String difficulty) {

        if (gameStarted) {
            return;
        }
        
        aiDifficulty = difficulty;
        gameStarted = true;
    }

    public String getAiDifficulty() {
        return aiDifficulty;
    }

    public String getTurnLog() {
        return turnLog;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

// Checks whether all projects have been completed.

    private boolean hasCompletedAllProjects(
        Company targetCompany) {
            
        if (targetCompany
                .getProjects()
                .isEmpty()) {
            return false;
        }
        
        if (!targetCompany
                .getAvailableProjects()
                .isEmpty()) {
            return false;
        }
        
        for (Project project
                : targetCompany.getProjects()) {

            if (!project.isFinished()) {
                return false;
            }
        }
        return true;
    }

// Determines whether the game should end and calculates the winner.

    private void advanceTurn() {
        
        boolean playerDone =
            hasCompletedAllProjects(
                company
            );
            
        boolean aiDone =
            hasCompletedAllProjects(
                aiCompany
            );
            
        if (!playerDone && !aiDone) {
            return;
        }
        
        gameOver = true;
        
        if (playerDone && aiDone) {
            
            if (company.getCash()
                    > aiCompany.getCash()) {

                winner = "PLAYER WINS";
            }
            
            else if (
                aiCompany.getCash()
                    > company.getCash()
            ) {
                winner = "AI WINS";
            }
            
            else {
                winner = "DRAW";
            }
        }
        
        else if (playerDone) {
            winner = "PLAYER WINS";
        }
        
        else {
            winner = "AI WINS";
        }
    }

// Distributes productivity across all active projects
// and updates project progress for the turn.

    private void progressAllProjects(
            Company targetCompany) {
                
        List<Project> activeProjects =
            new ArrayList<>();
            
        for (Project project
                : targetCompany.getProjects()) {
                    
            if (project.getStatus()
                    == ProjectStatus.PLANNED) {
                project.start();
            }
            
            if (project.getStatus()
                    == ProjectStatus.IN_PROGRESS) {
                activeProjects.add(project);
            }
        }
        
        if (activeProjects.isEmpty()) {
            return;
        }
        
        int remainingProductivity =
            targetCompany
                .calculateTotalProductivity();
                
        List<Project> unfinishedProjects =
            new ArrayList<>(activeProjects);
            
        for (Project project : activeProjects) {
            
            int remainingWork =
                project.getRequiredWork()
                - project.getProgress();
                
            if (remainingWork
                    <= remainingProductivity
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
            
            for (Project project
                    : unfinishedProjects) {
                
                project.workOneTurn(
                    productivityPerProject
                );
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
    
    public String getWinner() {
        return winner;
    }

// Resets the entire game to its initial state.

    public void restartGame() {
        
        company = new Company(
            "TechCorp",
            50000
        );
        
        aiCompany = new Company(
            "AI Corp",
            50000
        );
        
        turn = 1;
        
        aiDifficulty = "NORMAL";
        
        gameStarted = false;
        
        gameOver = false;
        
        winner = "";
        
        turnLog = "Game started.";
        
        company.hire(
            new Developer(
                "Anna",
                10,
                6000
            )
        );
        
        company.hire(
            new Tester(
                "Piotr",
                6,
                4000
            )
        );
        
        company.hire(
            new Manager(
                "Ewa",
                5,
                5000
            )
        );
        
        aiCompany.hire(
            new Developer(
                "AI Dev",
                10,
                6000
            )
        );
        
        aiCompany.hire(
            new Tester(
                "AI Tester",
                6,
                4000
            )
        );
        
        aiCompany.hire(
            new Manager(
                "AI Manager",
                5,
                5000
            )
        );
        
        addProjects(company);
        
        addProjects(aiCompany);
    }
}