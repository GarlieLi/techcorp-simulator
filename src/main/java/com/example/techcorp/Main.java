package com.example.techcorp;

public class Main {

    public static void main(String[] args) {
        Project mobileApp = new Project("Mobile App", 50);

        mobileApp.addWorker(new Developer("Anna", 9, 8000));
        mobileApp.addWorker(new Tester("Piotr", 6, 6500));
        mobileApp.addWorker(new Manager("Ewa", 7, 9000));

        mobileApp.addWorker(new AutomatedTool("CI Pipeline", 5));
        mobileApp.addWorker(new FreelancerBot("BotX", 4));

        mobileApp.start();

        while (!mobileApp.isFinished()) {
            mobileApp.workOneTurn();
            System.out.println(mobileApp.getName() + " | status: "
                + mobileApp.getStatus()
                + " | progress: "
                + mobileApp.getProgress() + "/" + mobileApp.getRequiredWork());
        }
    }
}