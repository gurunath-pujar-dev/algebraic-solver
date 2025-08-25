package com.equation.solver.repository;

import com.equation.solver.model.Equation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquationRepository extends JpaRepository<Equation, Long> {
    
    @Query("SELECT e FROM Equation e ORDER BY e.createdAt DESC")
    List<Equation> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT COUNT(e) FROM Equation e WHERE e.originalEquation = :equation")
    long countByOriginalEquation(@Param("equation") String equation);
    
    @Query("SELECT e FROM Equation e WHERE e.originalEquation = :equation")
    Optional<Equation> findByOriginalEquation(@Param("equation") String equation);
    
    boolean existsByOriginalEquation(String originalEquation);
}