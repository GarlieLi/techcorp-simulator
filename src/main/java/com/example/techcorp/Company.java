package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private double budget;

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    public Company(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }

    public void hire(Employee employee) {
        employees.add(employee);
    }

    public void startProject(Project project) {
        projects.add(project);
    }

    public void showStatus() {
        System.out.println("=== COMPANY STATUS ===");
        System.out.println("Name: " + name);
        System.out.println("Cash: " + budget);
        System.out.println("Employees: " + employees.size());
        System.out.println("Projects: " + projects.size());
        System.out.println();

        if (projects.isEmpty()) {
            System.out.println("No active projects.");
        } else {
            System.out.println("Projects:");
            for (Project project : projects) {
                System.out.println("- " + project.getName()
                + " | status: " + project.getStatus()
                + " | progress: " + project.getProgress() + "/" + project.getRequiredWork()
                + " | finished: " + project.isFinished());
            }
        }

        System.out.println("======================");
    }
}