package com.example.techcorp;

public class FreelancerBot implements Workable {

    private String name;
    private int efficiency;

    public FreelancerBot(String name, int efficiency) {
        this.name = name;
        this.efficiency = efficiency;
    }

    @Override
    public int work() {
        return efficiency;
    }

    public String getName() {
        return name;
    }
}