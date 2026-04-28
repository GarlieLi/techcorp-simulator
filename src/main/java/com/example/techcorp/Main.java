package com.example.techcorp;

public class Main {

    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50_000);

        company.hire(new Developer("Anna", 9, 8_000));
        company.hire(new Tester("Piotr", 6, 6_500));
        company.hire(new Manager("Ewa", 7, 9_000));

        System.out.println("=== Salary Overview ===");
        for (Employee employee : company.getEmployees()) {
            if (employee instanceof Payable payable) {
                System.out.println(employee.getName() + ": " + payable.calculateSalary());
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

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);

        engine.start();
    }
}