package com.example.techcorp;

public class Intern extends Employee {

    public Intern(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {

        int productivity = Math.max(1, getSkill() / 4);

        assert productivity > 0 :
            "Intern productivity should be positive.";

        return productivity;
    }

    @Override
    public String getRoleName() {
        return "Intern";
    }
}