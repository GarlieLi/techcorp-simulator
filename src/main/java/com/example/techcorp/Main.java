package com.example.techcorp;

public class Main {

    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50_000);

        company.hire(new Developer("Anna", 9, 8_000));
        company.hire(new Tester("Piotr", 6, 6_500));
        company.hire(new Manager("Ewa", 7, 9_000));

        Project mobileApp = new Project("Mobile App", 30);
        Project website = new Project("Website", 20);

        for (var employee : company.getEmployees()) {
            mobileApp.addWorker(employee); 
            website.addWorker(employee);
        }

        company.startProject(mobileApp);
        company.startProject(website);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);

        engine.start();
    }
}