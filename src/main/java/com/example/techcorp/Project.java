package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;

    private List<Employee> team = new ArrayList<>();

    public Project(String name, int requiredWork) {
        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
    }

    public void addEmployee(Employee employee) {
        team.add(employee);
    }

    public void workOneTurn() {

        for (Employee employee : team) {
            progress += employee.work();
        }

        if (progress > requiredWork) {
            progress = requiredWork;
        }
    }
    
    public boolean isFinished() {
        return progress >= requiredWork;
    }

     public int getProgress() {
         return progress;
    }

     public int getRequiredWork() {
        return requiredWork;
    }
}