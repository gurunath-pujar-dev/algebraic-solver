package com.equation.solver.model;

public class StoreEquationResponse {
    private String message;
    private String equationId;

    // Constructors
    public StoreEquationResponse() {}

    public StoreEquationResponse(String message, String equationId) {
        this.message = message;
        this.equationId = equationId;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getEquationId() { return equationId; }
    public void setEquationId(String equationId) { this.equationId = equationId; }
}