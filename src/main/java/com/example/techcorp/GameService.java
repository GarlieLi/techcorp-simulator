package com.example.techcorp;

public class GameService {

    private Company company;
    private Company aiCompany;

    private int turn;
    private String turnLog;

    public GameService() {

        company = new Company(
            "TechCorp",
            50000
        );

        turn = 1;
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
                
            int oldProgress =
                project.getProgress();
                    
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
            
        company.paySalaries();
            
        processAiTurn(log);
        
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

    private void processAiTurn(StringBuilder log) {
        
        if (!aiCompany
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
                    + (long) project.getReward()
                    + ")<br>"
                );
            }
        }
        aiCompany.collectProjectRewards();
        
        aiCompany.paySalaries();
    }

    public String getTurnLog() {
        return turnLog;
    }
}