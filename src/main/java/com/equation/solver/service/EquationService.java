//package com.equation.solver.service;
//
//import com.equation.solver.exception.EquationException;
//import com.equation.solver.model.Equation;
//import com.equation.solver.model.StoreEquationResponse;
//import com.equation.solver.model.TreeNode;
//import com.equation.solver.repository.EquationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class EquationService {
//    
//    @Autowired
//    private EquationRepository equationRepository;
//    
//    @Autowired
//    private PostfixTreeService postfixTreeService;
//    
//    public StoreEquationResponse storeEquation(String equationStr) {
//        try {
//            // Validate input
//            if (equationStr == null || equationStr.trim().isEmpty()) {
//                throw new EquationException("Equation cannot be empty");
//            }
//            
//            String cleanEquation = equationStr.trim();
//            
//            // Convert to postfix notation
//            String postfixNotation = postfixTreeService.convertToPostfix(cleanEquation);
//            
//            // Build postfix tree
//            TreeNode tree = postfixTreeService.buildPostfixTree(postfixNotation);
//            
//            // Serialize tree structure
//            String treeStructure = postfixTreeService.serializeTree(tree);
//            
//            // Create and save equation
//            Equation equation = new Equation(cleanEquation, postfixNotation, treeStructure);
//            Equation savedEquation = equationRepository.save(equation);
//            
//            return new StoreEquationResponse("Equation stored successfully", savedEquation.getId().toString());
//            
//        } catch (EquationException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EquationException("Failed to store equation: " + e.getMessage());
//        }
//    }
//    
//    @Transactional(readOnly = true)
//    public Map<String, Object> getAllEquations() {
//        try {
//            List<Equation> equations = equationRepository.findAllOrderByCreatedAtDesc();
//            
//            List<Map<String, String>> equationList = equations.stream()
//                .map(eq -> {
//                    Map<String, String> equationMap = new HashMap<>();
//                    equationMap.put("equationId", eq.getId().toString());
//                    equationMap.put("equation", eq.getOriginalEquation());
//                    return equationMap;
//                })
//                .toList();
//            
//            Map<String, Object> response = new HashMap<>();
//            response.put("equations", equationList);
//            
//            return response;
//            
//        } catch (Exception e) {
//            throw new EquationException("Failed to retrieve equations: " + e.getMessage());
//        }
//    }
//    
//    @Transactional(readOnly = true)
//    public StoreEquationResponse evaluateEquation(Long equationId, Map<String, Double> variables) {
//        try {
//            // Find equation
//            Optional<Equation> equationOpt = equationRepository.findById(equationId);
//            if (equationOpt.isEmpty()) {
//                throw new EquationException("Equation with ID " + equationId + " not found");
//            }
//            
//            Equation equation = equationOpt.get();
//            
//            // Deserialize tree structure
//            TreeNode tree = postfixTreeService.deserializeTree(equation.getTreeStructure());
//            
//            // Evaluate the tree
//            double result = postfixTreeService.evaluateTree(tree, variables);
//            
//            // Create response
//            StoreEquationResponse response = new StoreEquationResponse();
//            response.setEquationId(equation.getId().toString());
//            response.setEquation(equation.getOriginalEquation());
//            response.setVariables(variables);
//            response.setResult(result);
//            
//            return response;
//            
//        } catch (EquationException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EquationException("Failed to evaluate equation: " + e.getMessage());
//        }
//    }
//    
//    @Transactional(readOnly = true)
//    public boolean equationExists(Long id) {
//        return equationRepository.existsById(id);
//    }
//    
//    @Transactional(readOnly = true)
//    public long getEquationCount() {
//        return equationRepository.count();
//    }
//}

package com.equation.solver.service;

import com.equation.solver.exception.EquationException;
import com.equation.solver.model.Equation;
import com.equation.solver.model.StoreEquationResponse;
import com.equation.solver.model.EvaluateEquationResponse;
import com.equation.solver.model.TreeNode;
import com.equation.solver.repository.EquationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class EquationService {
    
    @Autowired
    private EquationRepository equationRepository;
    
    @Autowired
    private PostfixTreeService postfixTreeService;
    
    public StoreEquationResponse storeEquation(String equationStr) {
        try {
            // Validate input
            if (equationStr == null || equationStr.trim().isEmpty()) {
                throw new EquationException("Equation cannot be empty");
            }
            
            String cleanEquation = equationStr.trim().replaceAll("\\s+", "");
            
            // Check for duplicate equation
            if (equationRepository.existsByOriginalEquation(cleanEquation)) {
                // If duplicate exists, return the existing equation ID
                Optional<Equation> existingEquation = equationRepository.findByOriginalEquation(cleanEquation);
                if (existingEquation.isPresent()) {
                    return new StoreEquationResponse("Equation already exists", existingEquation.get().getId().toString());
                }
            }
            
            // Convert to postfix notation
            String postfixNotation = postfixTreeService.convertToPostfix(cleanEquation);
            
            // Build postfix tree
            TreeNode tree = postfixTreeService.buildPostfixTree(postfixNotation);
            
            // Serialize tree structure
            String treeStructure = postfixTreeService.serializeTree(tree);
            
            // Create and save equation
            Equation equation = new Equation(cleanEquation, postfixNotation, treeStructure);
            Equation savedEquation = equationRepository.save(equation);
            
            return new StoreEquationResponse("Equation stored successfully", savedEquation.getId().toString());
            
        } catch (EquationException e) {
            throw e;
        } catch (Exception e) {
            throw new EquationException("Failed to store equation: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getAllEquations() {
        try {
            List<Equation> equations = equationRepository.findAllOrderByCreatedAtDesc();
            
            List<Map<String, String>> equationList = equations.stream()
                .map(eq -> {
                    Map<String, String> equationMap = new HashMap<>();
                    equationMap.put("equationId", eq.getId().toString());
                    equationMap.put("equation", eq.getOriginalEquation());
                    return equationMap;
                })
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("equations", equationList);
            
            return response;
            
        } catch (Exception e) {
            throw new EquationException("Failed to retrieve equations: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public EvaluateEquationResponse evaluateEquation(Long equationId, Map<String, Double> variables) {
        try {
            // Find equation
            Optional<Equation> equationOpt = equationRepository.findById(equationId);
            if (equationOpt.isEmpty()) {
                throw new EquationException("Equation with ID " + equationId + " not found");
            }
            
            Equation equation = equationOpt.get();
            
            // Deserialize tree structure
            TreeNode tree = postfixTreeService.deserializeTree(equation.getTreeStructure());
            
            // Evaluate the tree
            double result = postfixTreeService.evaluateTree(tree, variables);
            
            // Create response
            return new EvaluateEquationResponse(
                equation.getId().toString(),
                equation.getOriginalEquation(),
                variables,
                result
            );
            
        } catch (EquationException e) {
            throw e;
        } catch (Exception e) {
            throw new EquationException("Failed to evaluate equation: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public boolean equationExists(Long id) {
        return equationRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public long getEquationCount() {
        return equationRepository.count();
    }
}