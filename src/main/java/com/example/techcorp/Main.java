package com.example.techcorp;

public class Main {

    public static void main(String[] args) {

        try {

            Company company = new Company("TechCorp", 50_000);

            company.hire(new Developer("Anna", 9, 7_000));
            company.hire(new Tester("Piotr", 6, 5_000));
            company.hire(new Manager("Ewa", 7, 6_500));

            assert !company.getEmployees().isEmpty() :
                "Company should have employees.";

            company.addAvailableProject(new Project("Website", 20, 30_000));
            company.addAvailableProject(new Project("AI Chatbot", 60, 90_000));
            company.addAvailableProject(new Project("Legacy Migration", 90, 120_000));
            company.addAvailableProject(new Project("Mobile App", 30, 45_000));

            assert company.getAvailableProjects().size() == 4 :
                "Available projects were not added correctly.";
                
            ConsoleUI ui = new ConsoleUI();
            GameEngine engine = new GameEngine(company, ui);
            
            engine.start();
        
        } catch (Exception e) {
            
            System.out.println(
                "Application error: " + e.getMessage()
            );
        }
    }
}