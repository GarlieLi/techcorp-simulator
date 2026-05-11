package com.example.techcorp;

public abstract class Employee {

    protected String name;
    protected int skill;
    protected double salary;

    public Employee(String name, int skill, double salary) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Employee name cannot be empty."
            );
        }

        if (skill <= 0) {
            throw new IllegalArgumentException(
                "Skill must be greater than 0."
            );
        }

         if (salary < 0) {
            throw new IllegalArgumentException(
                "Salary cannot be negative."
            );
        }

        this.name = name;
        this.skill = skill;
        this.salary = salary;

        assert this.name.equals(name) :
            "Name was not assigned correctly.";

        assert this.skill == skill :
            "Skill was not assigned correctly.";

        assert this.salary == salary :
            "Salary was not assigned correctly.";
    }

    public abstract int work();

    public abstract String getRoleName();

    public String getName() {
        return name;
    }

    public int getSkill() {
        return skill;
    }

    public double getSalary() {
        return salary;
    }
}