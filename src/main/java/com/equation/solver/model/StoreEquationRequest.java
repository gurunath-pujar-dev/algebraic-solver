package com.equation.solver.model;

import jakarta.validation.constraints.NotBlank;

public class StoreEquationRequest {
    @NotBlank(message = "Equation cannot be blank")
    private String equation;

    public StoreEquationRequest() {}

    public StoreEquationRequest(String equation) {
        this.equation = equation;
    }

    public String getEquation() { return equation; }
    public void setEquation(String equation) { this.equation = equation; }
}