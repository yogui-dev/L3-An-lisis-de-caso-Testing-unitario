package com.shop;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de unidad para Cart.
 */
class CartTest {

    private Cart cart;
    private Equipment board;
    private Equipment sail;
    private Map<String, Equipment> catalog;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        board = new Equipment("B1", "Board X", new BigDecimal("500.00"), 10, EquipmentType.BOARD, true);
        sail = new Equipment("S1", "Sail A", new BigDecimal("200.00"), 5, EquipmentType.SAIL, true);
        catalog = new HashMap<>();
        catalog.put(board.getId(), board);
        catalog.put(sail.getId(), sail);
    }

    @Test
    void addItemShouldAccumulateQuantitiesById() {
        cart.addItem(board, 1);
        cart.addItem(board, 2);
        assertFalse(cart.isEmpty());
        assertEquals(3, cart.getItems().get("B1"));
    }

    @Test
    void addItemShouldValidateInputs() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(null, 1));
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(board, 0));
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(board, -5));
    }

    @Test
    void estimateSubtotalShouldSumLineTotals() {
        cart.addItem(board, 1); // 500
        cart.addItem(sail, 3);  // 600
        assertEquals(new BigDecimal("1100.00"), cart.estimateSubtotal(catalog));
    }

    @Test
    void estimateSubtotalShouldFailWhenIdMissingInCatalog() {
        cart.addItem(board, 1);
        cart.addItem(sail, 1);
        catalog.remove("S1");
        assertThrows(IllegalArgumentException.class, () -> cart.estimateSubtotal(catalog));
    }
}
