package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;
    private List<Workable> team;
    private ProjectStatus status;

    public Project(String name, int requiredWork) {
        validateName(name);
        validateRequiredWork(requiredWork);

        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.team = new ArrayList<>();
        this.status = ProjectStatus.PLANNED;
    }

    public void addWorker(Workable workable) {
        if (workable == null) {
             throw new IllegalArgumentException("Worker cannotbe null.");
        }
        team.add(workable);
    }

    public void start() {
         if (status == ProjectStatus.PLANNED) {
         status = ProjectStatus.IN_PROGRESS;
         System.out.println("[" + name + "] Status changed: PLANNED → IN_PROGRESS");
        }
    }

    public void putOnHold() {
        if (status == ProjectStatus.IN_PROGRESS) {
            status = ProjectStatus.ON_HOLD;
            System.out.println("[" + name + "] Status changed: IN_PROGRESS → ON_HOLD");
        }
    }

    public void resume() {
        if (status == ProjectStatus.ON_HOLD) {
            status = ProjectStatus.IN_PROGRESS;
            System.out.println("[" + name + "] Status changed: ON_HOLD → IN_PROGRESS");
        }
    }

    public void cancel() {
         if (status != ProjectStatus.FINISHED) {
         status = ProjectStatus.CANCELLED;
         System.out.println("[" + name + "] Status changed: → CANCELLED");
        }
    }

    public void workOneTurn() {
        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }

        int workDone = 0;

        for (Workable workable: team) {
            workDone += workable.work();
        }

        progress += workDone;

        System.out.println("[" + name + "] +" + workDone + " progress → " + progress + "/" + requiredWork);

        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
            System.out.println("[" + name + "] Status changed: IN_PROGRESS → FINISHED");
        }
    }
    
    public boolean isFinished() {
        return status == ProjectStatus.FINISHED;
    }

    public String getName() {
        return name;
    }

    public int getRequiredWork() {
        return requiredWork;
    }

    public int getProgress() {
        return progress;
    }

    public List<Workable> getTeam() {
        return team;
    }

     public ProjectStatus getStatus() {
        return status;
    } 

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank.");
        }
    }

    private void validateRequiredWork(int requiredWork) {
        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be greater than 0.");
        }
    }

}