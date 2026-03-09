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
}