package com.shop;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de unidad para Equipment.
 */
class EquipmentTest {

    private Equipment board;

    @BeforeEach
    void setUp() {
        // Crear un equipo válido para reutilizar en pruebas
        board = new Equipment("B1", "Board X", new BigDecimal("499.90"), 5, EquipmentType.BOARD, true);
    }

    @AfterEach
    void tearDown() {
        // No hay recursos externos, pero se deja para demostrar ciclo de vida
        board = null;
    }

    @Test
    void constructorShouldNormalizePriceAndSetFields() {
        assertEquals("B1", board.getId());
        assertEquals("Board X", board.getName());
        assertEquals(new BigDecimal("499.90"), board.getPrice());
        assertEquals(5, board.getStock());
        assertEquals(EquipmentType.BOARD, board.getType());
        assertTrue(board.isRentalEligible());
        // Ejemplo adicional con AssertJ (opcional)
        assertThat(board.getPrice()).isEqualByComparingTo("499.90");
    }

    @Test
    void constructorShouldValidateInputs() {
        // name vacío
        assertThrows(IllegalArgumentException.class,
                () -> new Equipment("X", " ", new BigDecimal("1.00"), 1, EquipmentType.BOARD, false));
        // price negativo
        assertThrows(IllegalArgumentException.class,
                () -> new Equipment("X", "Ok", new BigDecimal("-0.01"), 1, EquipmentType.BOARD, false));
        // stock negativo
        assertThrows(IllegalArgumentException.class,
                () -> new Equipment("X", "Ok", new BigDecimal("0.00"), -1, EquipmentType.BOARD, false));
        // type nulo
        assertThrows(IllegalArgumentException.class,
                () -> new Equipment("X", "Ok", new BigDecimal("0.00"), 0, null, false));
    }

    @Test
    void decreaseStockShouldSucceedWhenEnoughStock() {
        board.decreaseStock(3);
        assertEquals(2, board.getStock());
    }

    @Test
    void decreaseStockShouldRejectZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> board.decreaseStock(0));
        assertThrows(IllegalArgumentException.class, () -> board.decreaseStock(-1));
    }

    @Test
    void decreaseStockShouldRejectWhenInsufficient() {
        assertThrows(IllegalArgumentException.class, () -> board.decreaseStock(6));
    }
}
