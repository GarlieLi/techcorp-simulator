package com.example.techcorp;

public class Main {

    public static void main(String[] args) {

        Company company = new Company("TechCorp", 50000);

        Employee anna = new Employee("Anna", 8, 7000);
        Employee piotr = new Employee("Piotr", 6, 6500);
        Employee maria = new Employee("Maria", 7, 6800);

        company.hire(anna);
        company.hire(piotr);
        company.hire(maria);

        Project project = new Project("Mobile App", 50);

        project.addEmployee(anna);
        project.addEmployee(piotr);
        project.addEmployee(maria);

        company.startProject(project);

        Project website = new Project("Website", 40);

        website.addEmployee(anna);
        website.addEmployee(maria);

        company.startProject(website);

        int turns = 0;

        while (!project.isFinished()) {
             project.workOneTurn();
             turns++;

             System.out.println(
                "Turn " + turns +
                 " | Progress: " + project.getProgress() +
                  "/" + project.getRequiredWork()
             );
        }

    System.out.println("Project completed in " + turns + " turns!");
    }
}