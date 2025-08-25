//package com.equation.solver.controller;
//
//import com.equation.solver.model.StoreEquationResponse;
//import com.equation.solver.model.EvaluateEquationRequest;
//import com.equation.solver.model.StoreEquationRequest;
//import com.equation.solver.service.EquationService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/equations")
//@CrossOrigin(origins = "*")
//public class EquationController {
//    
//    @Autowired
//    private EquationService equationService;
//    
//    @PostMapping("/store")
//    public ResponseEntity<StoreEquationResponse> storeEquation(
//            @Valid @RequestBody StoreEquationRequest request) {
//        
//        StoreEquationResponse response = equationService.storeEquation(request.getEquation());
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//    
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllEquations() {
//        Map<String, Object> response = equationService.getAllEquations();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    
//    @PostMapping("/{equationId}/evaluate")
//    public ResponseEntity<StoreEquationResponse> evaluateEquation(
//            @PathVariable Long equationId,
//            @Valid @RequestBody EvaluateEquationRequest request) {
//        
//        StoreEquationResponse response = equationService.evaluateEquation(equationId, request.getVariables());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    
//    @GetMapping("/health")
//    public ResponseEntity<Map<String, String>> healthCheck() {
//        Map<String, String> status = Map.of(
//            "status", "UP",
//            "service", "Algebraic Equation Solver",
//            "timestamp", java.time.LocalDateTime.now().toString()
//        );
//        return ResponseEntity.ok(status);
//    }
//}

package com.equation.solver.controller;

import com.equation.solver.model.StoreEquationResponse;
import com.equation.solver.model.EvaluateEquationResponse;
import com.equation.solver.model.EvaluateEquationRequest;
import com.equation.solver.model.StoreEquationRequest;
import com.equation.solver.service.EquationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/equations")
@CrossOrigin(origins = "*")
public class EquationController {
    
    @Autowired
    private EquationService equationService;
    
    @PostMapping("/store")
    public ResponseEntity<StoreEquationResponse> storeEquation(
            @Valid @RequestBody StoreEquationRequest request) {
        
        StoreEquationResponse response = equationService.storeEquation(request.getEquation());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEquations() {
        Map<String, Object> response = equationService.getAllEquations();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<EvaluateEquationResponse> evaluateEquation(
            @PathVariable Long equationId,
            @Valid @RequestBody EvaluateEquationRequest request) {
        
        EvaluateEquationResponse response = equationService.evaluateEquation(equationId, request.getVariables());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = Map.of(
            "status", "UP",
            "service", "Algebraic Equation Solver",
            "timestamp", java.time.LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(status);
    }
}