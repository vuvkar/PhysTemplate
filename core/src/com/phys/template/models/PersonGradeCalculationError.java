package com.phys.template.models;

public class PersonGradeCalculationError extends Error {
    public CalculationErrorType type;

    public PersonGradeCalculationError(CalculationErrorType type) {
        this.type = type;
    }
}

