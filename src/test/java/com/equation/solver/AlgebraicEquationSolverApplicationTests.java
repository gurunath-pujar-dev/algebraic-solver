package com.equation.solver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AlgebraicEquationSolverApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
    }
}