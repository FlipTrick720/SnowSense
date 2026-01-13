package com.notification.fitness;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit Test wrapper for Service Coupling Fitness Function
 */
public class ServiceCouplingFitnessFunctionTest {

    @Test
    @DisplayName("Service Layer Coupling - Maximum 3 dependencies per service")
    public void testServiceCoupling() {
        ServiceCouplingFitnessFunction fitnessFunction = new ServiceCouplingFitnessFunction();
        boolean passed = fitnessFunction.execute();
        
        assertTrue(passed, 
            "Service coupling fitness function failed. " +
            "One or more services exceed the maximum allowed dependencies.");
    }
}
