package com.equation.solver.model;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class EvaluateEquationRequest {
    @NotNull(message = "Variables cannot be null")
    private Map<String, Double> variables;

    public EvaluateEquationRequest() {}

    public EvaluateEquationRequest(Map<String, Double> variables) {
        this.variables = variables;
    }

    public Map<String, Double> getVariables() { return variables; }
    public void setVariables(Map<String, Double> variables) { this.variables = variables; }
}