//package com.equation.solver.service;
//
//import com.equation.solver.exception.EquationException;
//import com.equation.solver.model.Equation;
//import com.equation.solver.model.StoreEquationResponse;
//import com.equation.solver.repository.EquationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
//class EquationServiceTest {
//
//    @Mock
//    private EquationRepository equationRepository;
//
//    @Mock
//    private PostfixTreeService postfixTreeService;
//
//    @InjectMocks
//    private EquationService equationService;
//
//    private Equation testEquation;
//
//    @BeforeEach
//    void setUp() {
//        testEquation = new Equation();
//        testEquation.setId(1L);
//        testEquation.setOriginalEquation("3*x + 2*y - z");
//        testEquation.setPostfixNotation("3 x * 2 y * + z -");
//        testEquation.setTreeStructure("{\"value\":\"-\",\"operator\":true}");
//    }
//
//    @Test
//    void testStoreEquation_Success() {
//        String equation = "3*x + 2*y - z";
//        String postfix = "3 x * 2 y * + z -";
//        String treeStructure = "{\"value\":\"-\",\"operator\":true}";
//        
//        when(postfixTreeService.convertToPostfix(equation)).thenReturn(postfix);
//        when(postfixTreeService.buildPostfixTree(postfix)).thenReturn(new com.equation.solver.model.TreeNode());
//        when(postfixTreeService.serializeTree(any())).thenReturn(treeStructure);
//        when(equationRepository.save(any(Equation.class))).thenReturn(testEquation);
//
//        StoreEquationResponse response = equationService.storeEquation(equation);
//
//        assertNotNull(response);
//        assertEquals("Equation stored successfully", response.getMessage());
//        assertEquals("1", response.getEquationId());
//        
//        verify(equationRepository, times(1)).save(any(Equation.class));
//    }
//
//    @Test
//    void testStoreEquation_EmptyEquation() {
//        assertThrows(EquationException.class, () -> {
//            equationService.storeEquation("");
//        });
//        
//        assertThrows(EquationException.class, () -> {
//            equationService.storeEquation(null);
//        });
//    }
//
//    @Test
//    void testGetAllEquations() {
//        List<Equation> equations = List.of(testEquation);
//        when(equationRepository.findAllOrderByCreatedAtDesc()).thenReturn(equations);
//
//        Map<String, Object> response = equationService.getAllEquations();
//
//        assertNotNull(response);
//        assertTrue(response.containsKey("equations"));
//        
//        @SuppressWarnings("unchecked")
//        List<Map<String, String>> equationsList = (List<Map<String, String>>) response.get("equations");
//        assertEquals(1, equationsList.size());
//        assertEquals("1", equationsList.get(0).get("equationId"));
//        assertEquals("3*x + 2*y - z", equationsList.get(0).get("equation"));
//    }
//
//    @Test
//    void testEvaluateEquation_Success() {
//        Long equationId = 1L;
//        Map<String, Double> variables = Map.of("x", 2.0, "y", 3.0, "z", 1.0);
//        com.equation.solver.model.TreeNode mockTree = new com.equation.solver.model.TreeNode();
//        
//        when(equationRepository.findById(equationId)).thenReturn(Optional.of(testEquation));
//        when(postfixTreeService.deserializeTree(testEquation.getTreeStructure())).thenReturn(mockTree);
//        when(postfixTreeService.evaluateTree(mockTree, variables)).thenReturn(11.0);
//
//        StoreEquationResponse response = equationService.evaluateEquation(equationId, variables);
//
//        assertNotNull(response);
//        assertEquals("1", response.getEquationId());
//        assertEquals("3*x + 2*y - z", response.getEquation());
//        assertEquals(variables, response.getVariables());
//        assertEquals(11.0, response.getResult());
//    }
//
//    @Test
//    void testEvaluateEquation_EquationNotFound() {
//        Long equationId = 999L;
//        Map<String, Double> variables = Map.of("x", 2.0);
//        
//        when(equationRepository.findById(equationId)).thenReturn(Optional.empty());
//
//        assertThrows(EquationException.class, () -> {
//            equationService.evaluateEquation(equationId, variables);
//        });
//    }
//
//    @Test
//    void testEquationExists() {
//        Long existingId = 1L;
//        Long nonExistingId = 999L;
//        
//        when(equationRepository.existsById(existingId)).thenReturn(true);
//        when(equationRepository.existsById(nonExistingId)).thenReturn(false);
//
//        assertTrue(equationService.equationExists(existingId));
//        assertFalse(equationService.equationExists(nonExistingId));
//    }
//
//    @Test
//    void testGetEquationCount() {
//        when(equationRepository.count()).thenReturn(5L);
//
//        long count = equationService.getEquationCount();
//
//        assertEquals(5L, count);
//    }
//}

package com.equation.solver.service;

import com.equation.solver.exception.EquationException;
import com.equation.solver.model.Equation;
import com.equation.solver.model.StoreEquationResponse;
import com.equation.solver.model.EvaluateEquationResponse;
import com.equation.solver.repository.EquationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class EquationServiceTest {

    @Mock
    private EquationRepository equationRepository;

    @Mock
    private PostfixTreeService postfixTreeService;

    @InjectMocks
    private EquationService equationService;

    private Equation testEquation;

    @BeforeEach
    void setUp() {
        testEquation = new Equation();
        testEquation.setId(1L);
        testEquation.setOriginalEquation("3*x+2*y-z");
        testEquation.setPostfixNotation("3 x * 2 y * + z -");
        testEquation.setTreeStructure("{\"value\":\"-\",\"operator\":true}");
    }

    @Test
    void testStoreEquation_Success() {
        String equation = "3*x + 2*y - z";
        String cleanEquation = "3*x+2*y-z";
        String postfix = "3 x * 2 y * + z -";
        String treeStructure = "{\"value\":\"-\",\"operator\":true}";
        
        when(equationRepository.existsByOriginalEquation(cleanEquation)).thenReturn(false);
        when(postfixTreeService.convertToPostfix(cleanEquation)).thenReturn(postfix);
        when(postfixTreeService.buildPostfixTree(postfix)).thenReturn(new com.equation.solver.model.TreeNode());
        when(postfixTreeService.serializeTree(any())).thenReturn(treeStructure);
        when(equationRepository.save(any(Equation.class))).thenReturn(testEquation);

        StoreEquationResponse response = equationService.storeEquation(equation);

        assertNotNull(response);
        assertEquals("Equation stored successfully", response.getMessage());
        assertEquals("1", response.getEquationId());
        
        verify(equationRepository, times(1)).save(any(Equation.class));
    }

    @Test
    void testStoreEquation_DuplicateEquation() {
        String equation = "3*x + 2*y - z";
        String cleanEquation = "3*x+2*y-z";
        
        when(equationRepository.existsByOriginalEquation(cleanEquation)).thenReturn(true);
        when(equationRepository.findByOriginalEquation(cleanEquation)).thenReturn(Optional.of(testEquation));

        StoreEquationResponse response = equationService.storeEquation(equation);

        assertNotNull(response);
        assertEquals("Equation already exists", response.getMessage());
        assertEquals("1", response.getEquationId());
        
        verify(equationRepository, never()).save(any(Equation.class));
    }

    @Test
    void testStoreEquation_EmptyEquation() {
        assertThrows(EquationException.class, () -> {
            equationService.storeEquation("");
        });
        
        assertThrows(EquationException.class, () -> {
            equationService.storeEquation(null);
        });
    }

    @Test
    void testGetAllEquations() {
        List<Equation> equations = List.of(testEquation);
        when(equationRepository.findAllOrderByCreatedAtDesc()).thenReturn(equations);

        Map<String, Object> response = equationService.getAllEquations();

        assertNotNull(response);
        assertTrue(response.containsKey("equations"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> equationsList = (List<Map<String, String>>) response.get("equations");
        assertEquals(1, equationsList.size());
        assertEquals("1", equationsList.get(0).get("equationId"));
        assertEquals("3*x+2*y-z", equationsList.get(0).get("equation"));
    }

    @Test
    void testEvaluateEquation_Success() {
        Long equationId = 1L;
        Map<String, Double> variables = Map.of("x", 2.0, "y", 3.0, "z", 1.0);
        com.equation.solver.model.TreeNode mockTree = new com.equation.solver.model.TreeNode();
        
        when(equationRepository.findById(equationId)).thenReturn(Optional.of(testEquation));
        when(postfixTreeService.deserializeTree(testEquation.getTreeStructure())).thenReturn(mockTree);
        when(postfixTreeService.evaluateTree(mockTree, variables)).thenReturn(11.0);

        EvaluateEquationResponse response = equationService.evaluateEquation(equationId, variables);

        assertNotNull(response);
        assertEquals("1", response.getEquationId());
        assertEquals("3*x+2*y-z", response.getEquation());
        assertEquals(variables, response.getVariables());
        assertEquals(11.0, response.getResult());
    }

    @Test
    void testEvaluateEquation_EquationNotFound() {
        Long equationId = 999L;
        Map<String, Double> variables = Map.of("x", 2.0);
        
        when(equationRepository.findById(equationId)).thenReturn(Optional.empty());

        assertThrows(EquationException.class, () -> {
            equationService.evaluateEquation(equationId, variables);
        });
    }

    @Test
    void testEquationExists() {
        Long existingId = 1L;
        Long nonExistingId = 999L;
        
        when(equationRepository.existsById(existingId)).thenReturn(true);
        when(equationRepository.existsById(nonExistingId)).thenReturn(false);

        assertTrue(equationService.equationExists(existingId));
        assertFalse(equationService.equationExists(nonExistingId));
    }

    @Test
    void testGetEquationCount() {
        when(equationRepository.count()).thenReturn(5L);

        long count = equationService.getEquationCount();

        assertEquals(5L, count);
    }
}