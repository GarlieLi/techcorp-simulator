package com.example.techcorp;

public class FreelancerBot implements Workable {

    private String name;
    private int efficiency;

    public FreelancerBot(String name, int efficiency) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Bot name cannot be empty."
            );
        }

        if (efficiency <= 0) {
            throw new IllegalArgumentException(
                "Efficiency must be greater than 0."
            );
        }

        this.name = name;
        this.efficiency = efficiency;

        assert this.name.equals(name) :
            "Name was not assigned correctly.";

        assert this.efficiency == efficiency :
            "Efficiency was not assigned correctly.";
    }

    @Override
    public int work() {

        int result = efficiency;

         assert result > 0 :
            "Work result should always be positive.";

        return result;
    }

    public String getName() {
        return name;
    }
}