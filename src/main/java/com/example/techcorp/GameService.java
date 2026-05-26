package com.example.techcorp;

public class GameService {

    private Company company;
    private Company aiCompany;

    private int turn;

    private String turnLog;
    private String aiDifficulty;
    private String winner;

    private boolean gameStarted;
    private boolean gameOver;

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

    public String endTurn() {
        
        StringBuilder log =
            new StringBuilder();
        
        int productivity =
            company.calculateTotalProductivity();
        
        for (Project project
            : company.getProjects()) {
                    
            if (project.getStatus()
                == ProjectStatus.PLANNED) {
                project.start();
            }
                    
            project.workOneTurn(
                productivity
            );
                
            if (!project.isRewardPaid()
                && project.isFinished()) {
                
                log.append(
                    "PLAYER completed "
                    + project.getName()
                    + " (+"
                    + (long) project.getReward()
                    + ")<br>"
                );
            }
        }
            
        company.collectProjectRewards();
        
        try {
            company.paySalaries();

        } catch (IllegalStateException e) {
            
            gameOver = true;
            winner = "AI WINS";
            turnLog = "PLAYER BANKRUPT";

            return "Game Over";
        }
        
        processAiTurn(log);
        
        try {
            aiCompany.paySalaries();
        } catch (IllegalStateException e) {
            
            gameOver = true;
            winner = "PLAYER WINS";
            turnLog = "AI BANKRUPT";
            return "Game Over";
        }

        advanceTurn();
        
        turn++;
        
        turnLog = log.toString();
        
        return "Turn ended.";
    }

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
        
        return tool.getName()
            + " purchased.";
    }

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

    public Company getAiCompany() {
        return aiCompany;
    }

    private void processAiTurn(
            StringBuilder log) {
        
        int maxProjects;
        int maxBots;
        int maxTools;
        double aiSafetyCash;
        
        if (aiDifficulty.equals("EASY")) {
            maxProjects = 1;
            maxBots = 1;
            maxTools = 1;
            aiSafetyCash = 50000;
        }
        
        else if (aiDifficulty.equals("HARD")) {
            maxProjects = 3;
            maxBots = 3;
            maxTools = 3;
            aiSafetyCash = 20000;
        }
        
        else {
            maxProjects = 2;
            maxBots = 2;
            maxTools = 2;
            aiSafetyCash = 35000;
        }
        
        if (aiCompany.getProjects().size()
                < maxProjects
                &&
            !aiCompany
                .getAvailableProjects()
                .isEmpty()) {
                    
            Project project =
                aiCompany
                .getAvailableProjects()
                .get(0);

            aiCompany.acceptProject(
                project
            );

            log.append(
                "AI accepted "
                + project.getName()
                + "<br>"
            );
        }
        
        if (aiCompany
                .getFreelancerBots()
                .size() < maxBots
                &&
            aiCompany.getCash()
                > aiSafetyCash
                &&
            aiCompany.canAfford(
                18000)) {
                    
            FreelancerBot bot =
                new FreelancerBot(
                    aiCompany.nextFreelancerBotName(),
                    12
                );
                
            aiCompany.addFreelancerBot(bot);
            
            aiCompany.spendBudget(18000);
            
            log.append(
                "AI bought FreelancerBot<br>"
            );
        }
        
        if (aiCompany
                .getAutomatedTools()
                .size() < maxTools
                &&
            aiCompany.getCash()
                > aiSafetyCash
                &&
            aiCompany.canAfford(
                5000)) {
                    
            AutomatedTool tool =
                new AutomatedTool(
                    aiCompany.nextAutomatedToolName(),
                    8
                );
                
            aiCompany.addAutomatedTool(tool);
            
            aiCompany.spendBudget(5000);
            
            log.append(
                "AI bought AutomatedTool<br>"
            );
        }
        
        int productivity =
            aiCompany
            .calculateTotalProductivity();
            
        for (Project project
                : aiCompany.getProjects()) {

            if (project.getStatus()
                    == ProjectStatus.PLANNED) {

                project.start();
            }
            
            project.workOneTurn(
                productivity
            );
            
            if (!project.isRewardPaid()
                    && project.isFinished()) {

                log.append(
                    "AI completed "
                    + project.getName()
                    + " (+"
                    + (long)
                    project.getReward()
                    + ")<br>"
                );
            }
        }
        
        aiCompany.collectProjectRewards();
    }

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

    public boolean isGameOver() {
        return gameOver;
    }
    
    public String getWinner() {
        return winner;
    }
}