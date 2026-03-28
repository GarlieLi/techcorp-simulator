package com.example.techcorp;

public class Main {

    public static void main(String[] args) {
        Company company = new Company("TechCorp", 50_000);

        Employee anna = new Developer("Anna", 9, 8_000);
        Employee piotr = new Tester("Piotr", 6, 6_500);
        Employee ewa = new Manager("Ewa", 7, 9_000);

        company.hire(anna);
        company.hire(piotr);
        company.hire(ewa);

        Project mobileApp = new Project("Mobile App", 30);
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        mobileApp.addEmployee(ewa);

        company.startProject(mobileApp);

        company.showStatus();

        mobileApp.start();
        mobileApp.putOnHold();
        mobileApp.resume();

        while (!mobileApp.isFinished()) {
            mobileApp.workOneTurn();
            company.showStatus();
        }
    }
}
