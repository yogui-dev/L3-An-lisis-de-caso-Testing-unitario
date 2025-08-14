package com.shop;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite para ejecutar todas las clases de prueba requeridas.
 */
@Suite
@SelectClasses({
        EquipmentTest.class,
        CartTest.class,
        ParametrizedStockTest.class,
        AssumptionsTest.class,
        OrderServiceTest.class
})
public class SuiteAllTests {
    // Intencionalmente vacío: la anotación @Suite orquesta la ejecución
}
