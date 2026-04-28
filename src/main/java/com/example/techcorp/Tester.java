package com.example.techcorp;

public class Tester extends Employee implements Workable, Payable{

    public Tester(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill() / 2;
    }

    @Override
    public double calculateSalary() {
        return getSalary();
    }

    @Override
    public String getRoleName() {
        return "Tester";
    }
}