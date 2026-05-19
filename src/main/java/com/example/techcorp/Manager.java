package com.example.techcorp;

public class Manager extends Employee implements Workable, Payable {

    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {

        int productivity = getSkill() - 3;

        assert productivity > 0 :
            "Manager productivity should be positive.";

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
        return "Manager";
    }
}