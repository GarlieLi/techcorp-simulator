package com.example.techcorp;

public class Manager extends Employee {

    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill() / 3;
    }

    @Override
    public String getRoleName() {
    return "Manager";
    }
}