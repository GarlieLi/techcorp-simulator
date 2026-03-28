package com.example.techcorp;

public class AutomatedTool implements Workable {

    private String name;
    private int productivity;

    public AutomatedTool(String name, int productivity) {
        this.name = name;
        this.productivity = productivity;
    }

    @Override
    public int work() {
        return productivity;
    }

    public String getName() {
        return name;
    }
}