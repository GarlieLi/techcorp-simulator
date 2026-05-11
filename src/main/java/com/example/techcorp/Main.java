package com.example.techcorp;

public class Main {

    public static void main(String[] args) {

        try {

            Company company = new Company("TechCorp", 50_000);

            company.hire(new Developer("Anna", 9, 8_000));
            company.hire(new Tester("Piotr", 6, 6_500));
            company.hire(new Manager("Ewa", 7, 9_000));

            assert !company.getEmployees().isEmpty() :
                "Company should have employees.";
            
            System.out.println("=== Salary Overview ===");
            
            for (Employee employee : company.getEmployees()) {
                
                if (employee instanceof Payable payable) {
                    
                    System.out.println(
                        employee.getName()
                        + ": "
                        + payable.calculateSalary()
                    );
                }
            }
            
            System.out.println();
            
            Project mobileApp = new Project("Mobile App", 30);
            Project website = new Project("Website", 20);
            
            for (Employee employee : company.getEmployees()) {
                
                if (employee instanceof Workable workable) {
                    
                    mobileApp.addWorker(workable);
                    website.addWorker(workable);
                }
            }
            
            company.startProject(mobileApp);
            company.startProject(website);
            
            assert company.getProjects().size() == 2 :
                "Projects were not added correctly.";
                
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