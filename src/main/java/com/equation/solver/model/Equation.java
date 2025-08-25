package com.equation.solver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "equations")
public class Equation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Original equation cannot be blank")
    @Column(name = "original_equation", nullable = false, length = 1000)
    private String originalEquation;

    @Column(name = "postfix_notation", columnDefinition = "TEXT")
    private String postfixNotation;

    @Column(name = "tree_structure", columnDefinition = "TEXT")
    private String treeStructure;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Equation() {}

    public Equation(String originalEquation, String postfixNotation, String treeStructure) {
        this.originalEquation = originalEquation;
        this.postfixNotation = postfixNotation;
        this.treeStructure = treeStructure;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalEquation() { return originalEquation; }
    public void setOriginalEquation(String originalEquation) { this.originalEquation = originalEquation; }

    public String getPostfixNotation() { return postfixNotation; }
    public void setPostfixNotation(String postfixNotation) { this.postfixNotation = postfixNotation; }

    public String getTreeStructure() { return treeStructure; }
    public void setTreeStructure(String treeStructure) { this.treeStructure = treeStructure; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}