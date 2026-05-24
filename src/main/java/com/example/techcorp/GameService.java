package com.example.techcorp;

public class GameService {

    private Company company;

    public GameService() {

        company = new Company(
            "TechCorp",
            50000
        );

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
        
        return "Turn ended.";
    }
}