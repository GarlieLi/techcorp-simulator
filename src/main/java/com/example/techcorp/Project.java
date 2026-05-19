package com.example.techcorp;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;

    private double reward;
    private boolean rewardPaid;

    private List<Workable> team;
    private ProjectStatus status;

    public Project(String name, int requiredWork, double reward) {

        validateName(name);
        validateRequiredWork(requiredWork);
        validateReward(reward);

        this.name = name;
        this.requiredWork = requiredWork;

        this.reward = reward;
        this.rewardPaid = false;

        this.progress = 0;
        this.team = new ArrayList<>();
        this.status = ProjectStatus.PLANNED;

        assert this.progress == 0 :
            "Initial progress should be 0.";

        assert this.status == ProjectStatus.PLANNED :
            "Initial status should be PLANNED.";
    }

    public void addWorker(Workable workable) {

        if (workable == null) {
            throw new IllegalArgumentException(
                "Worker cannot be null."
            );
        }

         if (status == ProjectStatus.FINISHED
                || status == ProjectStatus.CANCELLED) {

            throw new IllegalStateException(
                "Cannot add workers to completed projects."
            );
        }

        team.add(workable);

        assert team.contains(workable) :
            "Worker was not added correctly.";
    }

    public void start() {
        
        if (status == ProjectStatus.PLANNED) {

            status = ProjectStatus.IN_PROGRESS;

            assert status == ProjectStatus.IN_PROGRESS;
        }
    }

    public void putOnHold() {

        if (status == ProjectStatus.IN_PROGRESS) {

            status = ProjectStatus.ON_HOLD;

            assert status == ProjectStatus.ON_HOLD;
        }
    }

    public void resume() {

        if (status == ProjectStatus.ON_HOLD) {
            
            status = ProjectStatus.IN_PROGRESS;

            assert status == ProjectStatus.IN_PROGRESS;
        }
    }

    public void cancel() {

        if (status != ProjectStatus.FINISHED) {

        status = ProjectStatus.CANCELLED;

        assert status == ProjectStatus.CANCELLED;
        }
    }

    public void workOneTurn() {

        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }

        int oldProgress = progress;
        int workDone = 0;

        for (Workable workable: team) {
            workDone += workable.work();
        }

        progress += workDone;

        if (progress > requiredWork) {
            progress = requiredWork;
        }

        assert progress >= oldProgress :
            "Project progress should not decrease.";

        assert progress <= requiredWork :
            "Project progress exceeded required work.";

        System.out.println(
            "[" + name + "] +"
            + workDone
            + " progress → "
            + progress
            + "/"
            + requiredWork
        );

        if (progress >= requiredWork) {

            status = ProjectStatus.FINISHED;

            assert status == ProjectStatus.FINISHED;
        }
    }

    public void workOneTurn(
        int productivity,
        String companyLabel) {
        
        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }
        
        int oldProgress = progress;
        progress += productivity;
        
        if (progress > requiredWork) {
            progress = requiredWork;
        }
        
        assert progress >= oldProgress :
        "Project progress should not decrease.";
        
        assert progress <= requiredWork :
        "Project progress exceeded required work.";
        
        if (progress >= requiredWork) {
            
        System.out.println(
            "[" + companyLabel + "] "
            + name
            + " completed"
        );
    } else {
        
        System.out.println(
            "[" + companyLabel + "] "
            + name
            + " "
            + progress
            + "/"
            + requiredWork
        );
    }
        if (progress >= requiredWork) {
            status = ProjectStatus.FINISHED;
            assert status == ProjectStatus.FINISHED;
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

        public double getReward() {
        return reward;
    }
        
    public boolean isRewardPaid() {
        return rewardPaid;
    }
    
    public void markRewardPaid() {
        rewardPaid = true;
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {

            throw new IllegalArgumentException(
                "Project name cannot be null or blank."
            );
        }
    }

    private void validateRequiredWork(int requiredWork) {

        if (requiredWork <= 0) {

            throw new IllegalArgumentException(
                "Required work must be greater than 0."
            );
        }
    }

    private void validateReward(double reward) {
        
        if (reward < 0) {
            
            throw new IllegalArgumentException(
                "Reward cannot be negative."
            );
        }
    }
}