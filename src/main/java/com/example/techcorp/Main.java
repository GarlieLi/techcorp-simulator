package com.example.techcorp;

/**
 * Entry point of the TechCorp Business Simulator.
 * Creates the initial company, employees, projects,
 * user interface, and starts the game.
 */

public class Main {

    public static void main(String[] args) {

        try {

            Company company = new Company("TechCorp", 50_000);

            company.hire(new Developer("Anna", 7, 6_000));
            company.hire(new Tester("Piotr", 6, 4_000));
            company.hire(new Manager("Ewa", 8, 5_000));

            assert !company.getEmployees().isEmpty() :
                "Company should have employees.";
            
            company.addAvailableProject(new Project("Bug Fix Sprint", 15, 18_000));
            company.addAvailableProject(new Project("Website", 20, 28_000));
            company.addAvailableProject(new Project("Mobile App", 30, 45_000));
            company.addAvailableProject(new Project("AI Chatbot", 60, 90_000));
            company.addAvailableProject(new Project("Cybersecurity Audit", 75, 110_000));
            company.addAvailableProject(new Project("Cloud Infrastructure", 110, 150_000));

            assert company.getAvailableProjects().size() == 6 :
                "Available projects were not added correctly.";
                
            ConsoleUI ui = new ConsoleUI();
            Difficulty difficulty = ui.chooseDifficulty();
            GameEngine engine = new GameEngine(
                company,
                ui,
                difficulty
            );
            
            engine.start();
        
        } catch (Exception e) {
            
            System.out.println(
                "Application error: " + e.getMessage()
            );
        }
    }
}