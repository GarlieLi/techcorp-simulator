package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private double budget;

    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Project> availableProjects = new ArrayList<>();

    private List<FreelancerBot> freelancerBots = new ArrayList<>();
    private List<AutomatedTool> automatedTools = new ArrayList<>();

    private int internCounter = 0;
    private int freelancerBotCounter = 0;
    private int automatedToolCounter = 0;

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

    public List<Project> getAvailableProjects() {
        return availableProjects;
    }

    public void addAvailableProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        availableProjects.add(project);
    }

    public void acceptProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        if (!availableProjects.remove(project)) {
            throw new IllegalStateException("Project is not available to accept.");
        }

        projects.add(project);
        assignWorkforceToProject(project);
    }

    public int countProjectsByStatus(ProjectStatus status) {

        int count = 0;

        for (Project project : projects) {

            if (project.getStatus() == status) {
                count++;
            }
        }

        return count;
    }

    private void assignWorkforceToProject(Project project) {

        for (Employee employee : employees) {

            if (employee instanceof Workable workable) {
                project.addWorker(workable);
            }
        }

        for (FreelancerBot bot : freelancerBots) {
            project.addWorker(bot);
        }

        for (AutomatedTool tool : automatedTools) {
            project.addWorker(tool);
        }
    }

    public List<FreelancerBot> getFreelancerBots() {
        return freelancerBots;
    }

    public List<AutomatedTool> getAutomatedTools() {
        return automatedTools;
    }

    public String nextInternName() {

        internCounter++;
        return "Intern" + internCounter;
    }

    public String nextFreelancerBotName() {

        freelancerBotCounter++;
        return "FreelancerBot" + freelancerBotCounter;
    }

    public String nextAutomatedToolName() {

        automatedToolCounter++;
        return "AutomatedTool" + automatedToolCounter;
    }

    public void addFreelancerBot(FreelancerBot bot) {

        if (bot == null) {
            throw new IllegalArgumentException("FreelancerBot cannot be null.");
        }

        freelancerBots.add(bot);
    }

    public void addAutomatedTool(AutomatedTool tool) {

        if (tool == null) {
            throw new IllegalArgumentException("AutomatedTool cannot be null.");
        }

        automatedTools.add(tool);
    }

    public void addEmployee(Employee employee) {
        hire(employee);
    }

    public void addProject(Project project) {
        startProject(project);
    }

    public int calculateTotalProductivity() {
        int total = 0;
        
        for (Employee employee : employees) {
            total += employee.work();
        }
        
        for (FreelancerBot bot : freelancerBots) {
            total += bot.work();
        }
        
        for (AutomatedTool tool : automatedTools) {
            total += tool.work();
        }
        
        return total;
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
    }
    
    public void collectProjectRewards() {
        
        for (Project project : projects) {
            
            if (project.isFinished()
                    && !project.isRewardPaid()) {
                
                budget += project.getReward();
                project.markRewardPaid();
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

        System.out.println("Employees (" + employees.size() + "):");
        
        if (employees.isEmpty()) {

            System.out.println("No employees.");
        } else {

            for (Employee employee : employees) {
                
                System.out.println(
                    "- " + employee.getName()
                    + " | role: " + employee.getRoleName()
                     + " | skill: " + employee.getSkill()
                     + " | salary: " + employee.getSalary()
                );
            }
        }
            
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