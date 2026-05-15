package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private double budget;

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    public Company(String name, double budget) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty.");
        }

        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }

        this.name = name;
        this.budget = budget;

        assert this.name.equals(name) :
            "Company name was not assigned correctly.";

        assert this.budget == budget :
            "Budget was not assigned correctly.";
    }

    public void hire(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        employees.add(employee);

        assert employees.contains(employee) :
            "Employee was not added correctly.";
    }

    public void startProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        projects.add(project);

        assert projects.contains(project) :
            "Project was not added correctly.";
    }

    public String getName() {
        return name;
    }

    public double getCash() {  
        return budget;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addEmployee(Employee employee) {
        hire(employee);
    }

    public void addProject(Project project) {
        startProject(project);
    }

    public double calculateTotalSalaries() {

        double total = 0;

        for (Employee e : employees) {
            if (e instanceof Payable p) {
                total += p.calculateSalary();
            }
        }

        assert total >= 0 :
            "Total salaries should never be negative.";

        return total;
    }

    public void paySalaries() {

        double total = calculateTotalSalaries();

        if (budget < total) {
            throw new IllegalStateException(
                    "Not enough budget to pay salaries."
            );
        }

        double oldBudget = budget;

        budget -= total;

        assert budget == oldBudget - total :
            "Salary payment calculation failed.";

        assert budget >= 0 :
            "Budget should never become negative.";

        System.out.println(
            "Salaries paid: "
            + total
            + " | Remaining cash: "
            + budget
        );
    }
    
    public void collectProjectRewards() {
        
        for (Project project : projects) {
            
            if (project.isFinished()
                && !project.isRewardPaid()) {

            budget += project.getReward();

            project.markRewardPaid();

            System.out.println(
                "Reward received from "
                + project.getName()
                + ": "
                + project.getReward());
            }
        }
    }
    
    public void spendBudget(double amount) {
        
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative."
            );
        }
        
        if (budget < amount) {
            throw new IllegalStateException(
                "Not enough budget."
            );
        }
        
        budget -= amount;
        
        assert budget >= 0 :
        "Budget should never become negative.";
    }

    public boolean canAfford(double amount) {
        
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative."
            );
        }
        
        return budget >= amount;
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

                System.out.println(
                    "- " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | progress: " + project.getProgress() + "/" + project.getRequiredWork()
                    + " | finished: " + project.isFinished()
                );
            }
        }

        System.out.println("======================");
    }
}