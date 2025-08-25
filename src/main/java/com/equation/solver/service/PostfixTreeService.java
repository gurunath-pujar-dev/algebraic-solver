//package com.equation.solver.service;
//
//import com.equation.solver.exception.EquationException;
//import com.equation.solver.model.TreeNode;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class PostfixTreeService {
//    
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    
//    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "^");
//    private static final Map<String, Integer> PRECEDENCE = Map.of(
//        "+", 1, "-", 1, "*", 2, "/", 2, "^", 3
//    );
//    
//    public String convertToPostfix(String infix) {
//        List<String> tokens = tokenize(infix);
//        List<String> postfix = new ArrayList<>();
//        Stack<String> operatorStack = new Stack<>();
//        
//        for (String token : tokens) {
//            if (isNumber(token) || isVariable(token)) {
//                postfix.add(token);
//            } else if (token.equals("(")) {
//                operatorStack.push(token);
//            } else if (token.equals(")")) {
//                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
//                    postfix.add(operatorStack.pop());
//                }
//                if (!operatorStack.isEmpty()) {
//                    operatorStack.pop(); // Remove the "("
//                }
//            } else if (OPERATORS.contains(token)) {
//                while (!operatorStack.isEmpty() && 
//                       !operatorStack.peek().equals("(") &&
//                       PRECEDENCE.getOrDefault(operatorStack.peek(), 0) >= PRECEDENCE.get(token)) {
//                    postfix.add(operatorStack.pop());
//                }
//                operatorStack.push(token);
//            }
//        }
//        
//        while (!operatorStack.isEmpty()) {
//            postfix.add(operatorStack.pop());
//        }
//        
//        return String.join(" ", postfix);
//    }
//    
//    public TreeNode buildPostfixTree(String postfix) {
//        String[] tokens = postfix.split("\\s+");
//        Stack<TreeNode> stack = new Stack<>();
//        
//        for (String token : tokens) {
//            TreeNode node = new TreeNode(token, OPERATORS.contains(token));
//            
//            if (node.isOperator()) {
//                if (stack.size() < 2) {
//                    throw new EquationException("Invalid postfix expression: insufficient operands for operator " + token);
//                }
//                TreeNode right = stack.pop();
//                TreeNode left = stack.pop();
//                node.addChild(left);
//                node.addChild(right);
//            }
//            
//            stack.push(node);
//        }
//        
//        if (stack.size() != 1) {
//            throw new EquationException("Invalid postfix expression: multiple root nodes");
//        }
//        
//        return stack.pop();
//    }
//    
//    public String serializeTree(TreeNode root) {
//        try {
//            return objectMapper.writeValueAsString(root);
//        } catch (JsonProcessingException e) {
//            throw new EquationException("Failed to serialize tree structure: " + e.getMessage());
//        }
//    }
//    
//    public TreeNode deserializeTree(String treeJson) {
//        try {
//            return objectMapper.readValue(treeJson, TreeNode.class);
//        } catch (JsonProcessingException e) {
//            throw new EquationException("Failed to deserialize tree structure: " + e.getMessage());
//        }
//    }
//    
//    public String reconstructInfix(TreeNode root) {
//        if (root == null) return "";
//        
//        if (!root.isOperator()) {
//            return root.getValue();
//        }
//        
//        if (root.getChildren().size() != 2) {
//            throw new EquationException("Invalid tree structure: operator must have exactly 2 children");
//        }
//        
//        String left = reconstructInfix(root.getChildren().get(0));
//        String right = reconstructInfix(root.getChildren().get(1));
//        String operator = root.getValue();
//        
//        // Add parentheses for clarity in complex expressions
//        if (needsParentheses(root.getChildren().get(0), root)) {
//            left = "(" + left + ")";
//        }
//        if (needsParentheses(root.getChildren().get(1), root)) {
//            right = "(" + right + ")";
//        }
//        
//        return left + " " + operator + " " + right;
//    }
//    
//    public double evaluateTree(TreeNode root, Map<String, Double> variables) {
//        if (root == null) {
//            throw new EquationException("Cannot evaluate null tree");
//        }
//        
//        if (!root.isOperator()) {
//            String value = root.getValue();
//            if (isNumber(value)) {
//                return Double.parseDouble(value);
//            } else if (isVariable(value)) {
//                if (!variables.containsKey(value)) {
//                    throw new EquationException("Variable '" + value + "' not provided");
//                }
//                return variables.get(value);
//            } else {
//                throw new EquationException("Unknown operand: " + value);
//            }
//        }
//        
//        if (root.getChildren().size() != 2) {
//            throw new EquationException("Invalid tree structure: operator must have exactly 2 children");
//        }
//        
//        double left = evaluateTree(root.getChildren().get(0), variables);
//        double right = evaluateTree(root.getChildren().get(1), variables);
//        
//        return switch (root.getValue()) {
//            case "+" -> left + right;
//            case "-" -> left - right;
//            case "*" -> left * right;
//            case "/" -> {
//                if (right == 0) throw new EquationException("Division by zero");
//                yield left / right;
//            }
//            case "^" -> Math.pow(left, right);
//            default -> throw new EquationException("Unknown operator: " + root.getValue());
//        };
//    }
//    
//    private List<String> tokenize(String infix) {
//        List<String> tokens = new ArrayList<>();
//        StringBuilder current = new StringBuilder();
//        
//        for (int i = 0; i < infix.length(); i++) {
//            char c = infix.charAt(i);
//            
//            if (Character.isWhitespace(c)) {
//                if (current.length() > 0) {
//                    tokens.add(current.toString());
//                    current.setLength(0);
//                }
//            } else if (OPERATORS.contains(String.valueOf(c)) || c == '(' || c == ')') {
//                if (current.length() > 0) {
//                    tokens.add(current.toString());
//                    current.setLength(0);
//                }
//                tokens.add(String.valueOf(c));
//            } else {
//                current.append(c);
//            }
//        }
//        
//        if (current.length() > 0) {
//            tokens.add(current.toString());
//        }
//        
//        return tokens;
//    }
//    
//    private boolean isNumber(String token) {
//        try {
//            Double.parseDouble(token);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//    
//    private boolean isVariable(String token) {
//        return token.matches("[a-zA-Z]\\w*");
//    }
//    
//    private boolean needsParentheses(TreeNode child, TreeNode parent) {
//        if (!child.isOperator() || !parent.isOperator()) {
//            return false;
//        }
//        
//        int childPrec = PRECEDENCE.getOrDefault(child.getValue(), 0);
//        int parentPrec = PRECEDENCE.getOrDefault(parent.getValue(), 0);
//        
//        return childPrec < parentPrec;
//    }
//}

package com.equation.solver.service;

import com.equation.solver.exception.EquationException;
import com.equation.solver.model.TreeNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostfixTreeService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "^");
    private static final Map<String, Integer> PRECEDENCE = Map.of(
        "+", 1, "-", 1, "*", 2, "/", 2, "^", 3
    );
    
    public String convertToPostfix(String infix) {
        List<String> tokens = tokenize(infix);
        List<String> postfix = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();
        
        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                postfix.add(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    postfix.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop(); // Remove the "("
                }
            } else if (OPERATORS.contains(token)) {
                while (!operatorStack.isEmpty() && 
                       !operatorStack.peek().equals("(") &&
                       PRECEDENCE.getOrDefault(operatorStack.peek(), 0) >= PRECEDENCE.get(token)) {
                    postfix.add(operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }
        
        while (!operatorStack.isEmpty()) {
            postfix.add(operatorStack.pop());
        }
        
        return String.join(" ", postfix);
    }
    
    public TreeNode buildPostfixTree(String postfix) {
        String[] tokens = postfix.split("\\s+");
        Stack<TreeNode> stack = new Stack<>();
        
        for (String token : tokens) {
            TreeNode node = new TreeNode(token, OPERATORS.contains(token));
            
            if (node.isOperator()) {
                if (stack.size() < 2) {
                    throw new EquationException("Invalid postfix expression: insufficient operands for operator " + token);
                }
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                node.addChild(left);
                node.addChild(right);
            }
            
            stack.push(node);
        }
        
        if (stack.size() != 1) {
            throw new EquationException("Invalid postfix expression: multiple root nodes");
        }
        
        return stack.pop();
    }
    
    public String serializeTree(TreeNode root) {
        try {
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new EquationException("Failed to serialize tree structure: " + e.getMessage());
        }
    }
    
    public TreeNode deserializeTree(String treeJson) {
        try {
            return objectMapper.readValue(treeJson, TreeNode.class);
        } catch (JsonProcessingException e) {
            throw new EquationException("Failed to deserialize tree structure: " + e.getMessage());
        }
    }
    
    public String reconstructInfix(TreeNode root) {
        if (root == null) return "";
        
        if (!root.isOperator()) {
            return root.getValue();
        }
        
        if (root.getChildren().size() != 2) {
            throw new EquationException("Invalid tree structure: operator must have exactly 2 children");
        }
        
        String left = reconstructInfix(root.getChildren().get(0));
        String right = reconstructInfix(root.getChildren().get(1));
        String operator = root.getValue();
        
        // Add parentheses for clarity in complex expressions
        if (needsParentheses(root.getChildren().get(0), root)) {
            left = "(" + left + ")";
        }
        if (needsParentheses(root.getChildren().get(1), root)) {
            right = "(" + right + ")";
        }
        
        return left + " " + operator + " " + right;
    }
    
    public double evaluateTree(TreeNode root, Map<String, Double> variables) {
        if (root == null) {
            throw new EquationException("Cannot evaluate null tree");
        }
        
        if (!root.isOperator()) {
            String value = root.getValue();
            if (isNumber(value)) {
                return Double.parseDouble(value);
            } else if (isVariable(value)) {
                if (!variables.containsKey(value)) {
                    throw new EquationException("Variable '" + value + "' not provided");
                }
                return variables.get(value);
            } else {
                throw new EquationException("Unknown operand: " + value);
            }
        }
        
        if (root.getChildren().size() != 2) {
            throw new EquationException("Invalid tree structure: operator must have exactly 2 children");
        }
        
        double left = evaluateTree(root.getChildren().get(0), variables);
        double right = evaluateTree(root.getChildren().get(1), variables);
        
        return switch (root.getValue()) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> {
                if (right == 0) throw new EquationException("Division by zero");
                yield left / right;
            }
            case "^" -> Math.pow(left, right);
            default -> throw new EquationException("Unknown operator: " + root.getValue());
        };
    }
    
    private List<String> tokenize(String infix) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (Character.isWhitespace(c)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else if (OPERATORS.contains(String.valueOf(c)) || c == '(' || c == ')') {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            tokens.add(current.toString());
        }
        
        return tokens;
    }
    
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isVariable(String token) {
        return token.matches("[a-zA-Z]\\w*");
    }
    
    private boolean needsParentheses(TreeNode child, TreeNode parent) {
        if (!child.isOperator() || !parent.isOperator()) {
            return false;
        }
        
        int childPrec = PRECEDENCE.getOrDefault(child.getValue(), 0);
        int parentPrec = PRECEDENCE.getOrDefault(parent.getValue(), 0);
        
        return childPrec < parentPrec;
    }
}