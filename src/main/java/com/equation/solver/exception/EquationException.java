package com.equation.solver.exception;

public class EquationException extends RuntimeException {
    public EquationException(String message) {
        super(message);
    }
    
    public EquationException(String message, Throwable cause) {
        super(message, cause);
    }
}