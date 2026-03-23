package com.example.techcorp;

public class Main {

    public static void main(String[] args) {

        Company company = new Company("TechCorp", 50000);

        Employee anna = new Developer("Anna", 8, 7000);
        Employee piotr = new Tester("Piotr", 6, 6500);
        Employee maria = new Manager("Maria", 9, 9000);
        Employee tom = new Intern("Tom", 4, 2000); 

        company.hire(anna);
        company.hire(piotr);
        company.hire(maria);
        company.hire(tom);

        company.printStatus();

        Project project = new Project("Mobile App", 50);

        project.addEmployee(anna);
        project.addEmployee(piotr);
        project.addEmployee(maria);

        company.startProject(project);

        Project website = new Project("Website", 40);

        website.addEmployee(anna);
        website.addEmployee(maria);
        website.addEmployee(tom);

        company.startProject(website);

        int turns = 0;

        project.start();
        website.start(); 

        while (!project.isFinished() || !website.isFinished()) {

            turns++;

            if (!project.isFinished()) {
                project.workOneTurn();
            }

            if (!website.isFinished()) {
                website.workOneTurn();
            }

            System.out.println("Turn " + turns);

            System.out.println(
                "Mobile App: " +
                project.getProgress() +
                "/" +
                project.getRequiredWork()
            );

            System.out.println(
                "Website: " +
                website.getProgress() +
                "/" +
                website.getRequiredWork()
            );

            System.out.println();
        }

        System.out.println("Simulation finished.");
    }
}