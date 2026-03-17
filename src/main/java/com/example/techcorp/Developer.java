package com.example.techcorp;

public class Developer extends Employee {

    public Developer(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return getSkill();
    }

    @Override
    public String getRoleName() {
        return "Developer";
    }
}