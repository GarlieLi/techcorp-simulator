package com.example.techcorp;

public abstract class Employee {

    protected String name;
    protected int skill;
    protected double salary;

    public Employee(String name, int skill, double salary) {
        this.name = name;
        this.skill = skill;
        this.salary = salary;
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