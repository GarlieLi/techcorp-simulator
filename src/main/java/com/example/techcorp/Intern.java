package com.example.techcorp;

public class Intern extends Employee
    implements Workable, Payable {

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
    public double calculateSalary() {

        double result = getSalary();

        assert result >= 0 :
        "Salary should not be negative.";

        return result;
    }

    @Override
    public String getRoleName() {
        return "Intern";
    }
}