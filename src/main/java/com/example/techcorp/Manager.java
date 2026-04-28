package com.example.techcorp;

public class Manager extends Employee implements Workable, Payable {

    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill() / 3;
    }

    @Override
    public double calculateSalary() {
        return getSalary();
    }

    @Override
    public String getRoleName() {
        return "Manager";
    }
}