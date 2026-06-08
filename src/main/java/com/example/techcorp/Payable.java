package com.example.techcorp;

/**
 * Interface for entities that receive payment
 * and have associated salary costs.
 */

public interface Payable {
    double calculateSalary();
}