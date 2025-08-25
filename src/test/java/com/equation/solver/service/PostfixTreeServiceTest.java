package com.equation.solver.service;

import com.equation.solver.exception.EquationException;
import com.equation.solver.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PostfixTreeServiceTest {

    private PostfixTreeService postfixTreeService;

    @BeforeEach
    void setUp() {
        postfixTreeService = new PostfixTreeService();
    }

    @Test
    void testConvertToPostfix_SimpleExpression() {
        String infix = "3 * x + 2 * y - z";
        String expected = "3 x * 2 y * + z -";
        String actual = postfixTreeService.convertToPostfix(infix);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertToPostfix_WithParentheses() {
        String infix = "(x + y) * (a - b)";
        String expected = "x y + a b - *";
        String actual = postfixTreeService.convertToPostfix(infix);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertToPostfix_WithExponents() {
        String infix = "x ^ 2 + y ^ 2";
        String expected = "x 2 ^ y 2 ^ +";
        String actual = postfixTreeService.convertToPostfix(infix);
        assertEquals(expected, actual);
    }

    @Test
    void testBuildPostfixTree_SimpleExpression() {
        String postfix = "3 x * 2 y * + z -";
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        
        assertNotNull(root);
        assertTrue(root.isOperator());
        assertEquals("-", root.getValue());
        assertEquals(2, root.getChildren().size());
    }

    @Test
    void testBuildPostfixTree_InvalidExpression() {
        String postfix = "3 x * +"; // Missing operand
        assertThrows(EquationException.class, () -> {
            postfixTreeService.buildPostfixTree(postfix);
        });
    }

    @Test
    void testEvaluateTree_SimpleExpression() {
        String postfix = "3 x * 2 y * + z -";
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        
        Map<String, Double> variables = Map.of("x", 2.0, "y", 3.0, "z", 1.0);
        double result = postfixTreeService.evaluateTree(root, variables);
        
        assertEquals(11.0, result, 0.001); // 3*2 + 2*3 - 1 = 6 + 6 - 1 = 11
    }

    @Test
    void testEvaluateTree_WithExponents() {
        String postfix = "x 2 ^ y 2 ^ +";
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        
        Map<String, Double> variables = Map.of("x", 3.0, "y", 4.0);
        double result = postfixTreeService.evaluateTree(root, variables);
        
        assertEquals(25.0, result, 0.001); // 3^2 + 4^2 = 9 + 16 = 25
    }

    @Test
    void testEvaluateTree_DivisionByZero() {
        String postfix = "x 0 /";
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        
        Map<String, Double> variables = Map.of("x", 5.0);
        assertThrows(EquationException.class, () -> {
            postfixTreeService.evaluateTree(root, variables);
        });
    }

    @Test
    void testEvaluateTree_MissingVariable() {
        String postfix = "x y +";
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        
        Map<String, Double> variables = Map.of("x", 5.0); // Missing 'y'
        assertThrows(EquationException.class, () -> {
            postfixTreeService.evaluateTree(root, variables);
        });
    }

    @Test
    void testReconstructInfix() {
        String originalInfix = "3 * x + 2 * y - z";
        String postfix = postfixTreeService.convertToPostfix(originalInfix);
        TreeNode root = postfixTreeService.buildPostfixTree(postfix);
        String reconstructed = postfixTreeService.reconstructInfix(root);
        
        assertNotNull(reconstructed);
        // Note: The reconstructed infix might have different spacing/parentheses
        // but should be mathematically equivalent
    }

    @Test
    void testSerializeAndDeserializeTree() {
        String postfix = "x y +";
        TreeNode originalRoot = postfixTreeService.buildPostfixTree(postfix);
        
        String serialized = postfixTreeService.serializeTree(originalRoot);
        TreeNode deserializedRoot = postfixTreeService.deserializeTree(serialized);
        
        assertNotNull(deserializedRoot);
        assertEquals(originalRoot.getValue(), deserializedRoot.getValue());
        assertEquals(originalRoot.isOperator(), deserializedRoot.isOperator());
    }
}