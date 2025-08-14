package com.shop;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas parametrizadas para decreaseStock de Equipment.
 */
class ParametrizedStockTest {

    @ParameterizedTest(name = "decreaseStock with valid qty={0} should reduce stock")
    @ValueSource(ints = {1, 2, 5})
    void decreaseStockValidQuantities(int qty) {
        Equipment sail = new Equipment("S1", "Sail A", new BigDecimal("200.00"), 10, EquipmentType.SAIL, true);
        int before = sail.getStock();
        sail.decreaseStock(qty);
        assertEquals(before - qty, sail.getStock());
    }

    @ParameterizedTest(name = "decreaseStock with invalid qty={0} should throw")
    @ValueSource(ints = {0, -1, -5})
    void decreaseStockInvalidQuantities(int qty) {
        Equipment mast = new Equipment("M1", "Mast Z", new BigDecimal("150.00"), 3, EquipmentType.MAST, false);
        assertThrows(IllegalArgumentException.class, () -> mast.decreaseStock(qty));
    }
}
