package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;
    private ProjectStatus status;

    private List<Employee> team = new ArrayList<>();

    public Project(String name, int requiredWork) {
        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.status = ProjectStatus.PLANNED;
    }

    public void addEmployee(Employee employee) {
        team.add(employee);
    }

     public void start() {
         if (status == ProjectStatus.PLANNED) {
         status = ProjectStatus.IN_PROGRESS;
         System.out.println(name + " started.");
        }
    }

     public void cancel() {
         if (status != ProjectStatus.FINISHED) {
         status = ProjectStatus.CANCELLED;
         System.out.println(name + " cancelled.");
        }
    }

    public void putOnHold() {
         if (status == ProjectStatus.IN_PROGRESS) {
         status = ProjectStatus.ON_HOLD;
        }
    }

    public void resume() {
         if (status == ProjectStatus.ON_HOLD) {
         status = ProjectStatus.IN_PROGRESS;
        }
    }

    public void workOneTurn() {

        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }

        for (Employee employee : team) {
            progress += employee.work();
        }

        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
            System.out.println(name + " is finished!");
        }
    }
    
    public boolean isFinished() {
        return status == ProjectStatus.FINISHED;
    }

     public ProjectStatus getStatus() {
        return status;
    } 

     public int getProgress() {
         return progress;
    }

     public int getRequiredWork() {
        return requiredWork;
    }
}