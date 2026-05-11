package com.example.techcorp;

public class AutomatedTool implements Workable {

    private String name;
    private int productivity;

    public AutomatedTool(String name, int productivity) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tool name cannot be empty.");
        }

        if (productivity <= 0) {
            throw new IllegalArgumentException(
                "Productivity must be greater than 0."
            );
        }

        this.name = name;
        this.productivity = productivity;

        assert this.name.equals(name) : "Name was not assigned correctly.";
        assert this.productivity == productivity :
            "Productivity was not assigned correctly.";
    }

    @Override
    public int work() {

        int result = productivity;

        assert result > 0 : "Work result should always be positive.";

        return result;
    }

    public String getName() {
        return name;
    }
}