package com.example.techcorp;

public abstract class Task {

    protected String title;

    public Task(String title) {
        this.title = title;
    }

    public abstract void execute();
}