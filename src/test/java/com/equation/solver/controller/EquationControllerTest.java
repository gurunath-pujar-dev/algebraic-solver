package com.equation.solver.controller;

import com.equation.solver.model.StoreEquationResponse;
import com.equation.solver.model.EvaluateEquationResponse;
import com.equation.solver.service.EquationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquationController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class EquationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquationService equationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testStoreEquation_Success() throws Exception {
        StoreEquationResponse mockResponse = new StoreEquationResponse("Equation stored successfully", "1");
        when(equationService.storeEquation(anyString())).thenReturn(mockResponse);

        String requestBody = "{\"equation\":\"3*x + 2*y - z\"}";

        mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Equation stored successfully"))
                .andExpect(jsonPath("$.equationId").value("1"));
    }

    @Test
    void testStoreEquation_InvalidInput() throws Exception {
        String requestBody = "{\"equation\":\"\"}";

        mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEquations() throws Exception {
        Map<String, Object> mockResponse = Map.of(
            "equations", List.of(
                Map.of("equationId", "1", "equation", "3*x + 2*y - z"),
                Map.of("equationId", "2", "equation", "x^2 + y^2 - 4")
            )
        );
        when(equationService.getAllEquations()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/equations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equations").isArray())
                .andExpect(jsonPath("$.equations[0].equationId").value("1"))
                .andExpect(jsonPath("$.equations[0].equation").value("3*x + 2*y - z"));
    }

    @Test
    void testEvaluateEquation_Success() throws Exception {
        EvaluateEquationResponse mockResponse = new EvaluateEquationResponse();
        mockResponse.setEquationId("1");
        mockResponse.setEquation("3*x + 2*y - z");
        mockResponse.setVariables(Map.of("x", 2.0, "y", 3.0, "z", 1.0));
        mockResponse.setResult(11.0);
        
        when(equationService.evaluateEquation(eq(1L), any(Map.class))).thenReturn(mockResponse);

        String requestBody = "{\"variables\":{\"x\":2,\"y\":3,\"z\":1}}";

        mockMvc.perform(post("/api/equations/1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equationId").value("1"))
                .andExpect(jsonPath("$.equation").value("3*x + 2*y - z"))
                .andExpect(jsonPath("$.result").value(11.0));
    }

    @Test
    void testEvaluateEquation_InvalidInput() throws Exception {
        String requestBody = "{\"variables\":null}";

        mockMvc.perform(post("/api/equations/1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/equations/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Algebraic Equation Solver"));
    }
}