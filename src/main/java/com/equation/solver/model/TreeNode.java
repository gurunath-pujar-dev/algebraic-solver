package com.equation.solver.model;

import com.fasterxml.jackson.annotation.JsonIgnore; 

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String value;
    private boolean isOperator;
    private List<TreeNode> children;
    
    @JsonIgnore
    private TreeNode parent;

    public TreeNode(String value, boolean isOperator) {
        this.value = value;
        this.isOperator = isOperator;
        this.children = new ArrayList<>();
    }

    // Constructors
    public TreeNode() {
        this.children = new ArrayList<>();
    }

    // Getters and Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public boolean isOperator() { return isOperator; }
    public void setOperator(boolean operator) { isOperator = operator; }
    
    public List<TreeNode> getChildren() { return children; }
    public void setChildren(List<TreeNode> children) { this.children = children; }
    
    public TreeNode getParent() { return parent; }
    public void setParent(TreeNode parent) { this.parent = parent; }

    public void addChild(TreeNode child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public String toString() {
        return "TreeNode{value='" + value + "', isOperator=" + isOperator + ", children=" + children.size() + "}";
    }
}