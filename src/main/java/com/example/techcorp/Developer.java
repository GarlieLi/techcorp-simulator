package com.example.techcorp;

public class Developer extends Employee implements Workable, Payable{

    public Developer(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill() * 2;
    }

    @Override
    public double calculateSalary() {
        return getSalary();
    }

    @Override
    public String getRoleName() {
        return "Developer";
    }
}