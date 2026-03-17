package com.example.techcorp;

public class Intern extends Employee {

    public Intern(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill() / 4;
    }

    @Override
    public String getRoleName() {
        return "Intern";
    }
}