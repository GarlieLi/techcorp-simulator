package com.example.techcorp;

public class Employee {

    private String name;
    private int skill;
    private double salary;

    public Employee(String name, int skill, double salary) {
        this.name = name;
        this.skill = skill;
        this.salary = salary;
    }

    public int work() {
        return skill;
    }
}