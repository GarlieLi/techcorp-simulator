package com.example.techcorp;

public class GameService {

    private Company company;

    private int turn;

    public GameService() {

        company = new Company(
            "TechCorp",
            50000
        );

        turn = 1;

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

        company.addAvailableProject(
            new Project(
                "Bug Fix Sprint",
                15,
                18000
            )
        );

        company.addAvailableProject(
            new Project(
                "Website",
                20,
                28000
            )
        );

        company.addAvailableProject(
            new Project(
                "Mobile App",
                30,
                45000
            )
        );

        company.addAvailableProject(
            new Project(
                "AI Chatbot",
                60,
                90000
            )
        );

        company.addAvailableProject(
            new Project(
                "Cybersecurity Audit",
                75,
                110000
            )
        );

        company.addAvailableProject(
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
        }
        
        company.collectProjectRewards();
        
        company.paySalaries();

        turn++;
        
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
}