package com.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Demostración de assumptions: saltamos la prueba si no estamos en entorno CI.
 * Usa variable de entorno CI=true para ejecutarse.
 */
class AssumptionsTest {

    @Test
    void onlyRunOnCI() {
        // Si la variable CI no es "true", se asume false y se salta la prueba.
        assumeTrue("true".equalsIgnoreCase(System.getenv("CI")),
                () -> "Skipping test because CI env var is not true");
        // Si estamos en CI, esta aserción se evaluará.
        assertTrue(true);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "RUN_EXTRA", matches = "yes", disabledReason = "RUN_EXTRA must be 'yes'")
    void conditionallyEnabledByEnv() {
        // Se ejecuta sólo si RUN_EXTRA=yes
        assertEquals(2, 1 + 1);
    }
}
