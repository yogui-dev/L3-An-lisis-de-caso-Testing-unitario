package com.shop;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de unidad para OrderService, usando Hamcrest para algunas aserciones expresivas.
 */
class OrderServiceTest {

    private Map<String, Equipment> catalog;
    private Cart cart;
    private OrderService service;

    private Equipment board; // elegible
    private Equipment mast;  // no elegible

    @BeforeEach
    void setUp() {
        catalog = new HashMap<>();
        board = new Equipment("B1", "Board X", new BigDecimal("500.00"), 10, EquipmentType.BOARD, true);
        mast = new Equipment("M1", "Mast Z", new BigDecimal("150.00"), 2, EquipmentType.MAST, false);
        catalog.put(board.getId(), board);
        catalog.put(mast.getId(), mast);
        cart = new Cart();
        service = new OrderService(catalog);
    }

    @AfterEach
    void tearDown() {
        catalog.clear();
    }

    @Test
    void processOrderShouldFailWhenCartEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.processOrder(cart, false));
    }

    @Test
    void processOrderShouldFailWhenEquipmentMissing() {
        cart.addItem(new Equipment("X", "Temp", new BigDecimal("1.00"), 1, EquipmentType.BOARD, true), 1);
        // Quitar del catálogo a propósito para simular inexistente
        assertThrows(IllegalArgumentException.class, () -> service.processOrder(cart, false));
    }

    @Test
    void processOrderShouldFailWhenInsufficientStock() {
        cart.addItem(mast, 3); // mast stock = 2
        assertThrows(IllegalArgumentException.class, ()-> service.processOrder(cart, false));
    }

    @Test
    void processOrderShouldFailWhenRentalNotEligible() {
        cart.addItem(mast, 1); // mast rentalEligible=false
        assertThrows(IllegalArgumentException.class, ()-> service.processOrder(cart, true));
    }

    @Test
    void processOrderPurchaseShouldDecreaseStockAndReturnReceipt() {
        cart.addItem(board, 2); // 2 * 500 = 1000
        OrderReceipt receipt = service.processOrder(cart, false);

        // Validar recibo con Hamcrest
        assertThat(receipt.getStatus(), is("OK"));
        assertThat(receipt.isRental(), is(false));
        assertThat(receipt.getSubtotal(), comparesEqualTo(new BigDecimal("1000.00")));
        assertThat(receipt.getDeposit(), comparesEqualTo(new BigDecimal("0.00")));
        assertThat(receipt.getTotal(), comparesEqualTo(new BigDecimal("1000.00")));
        assertThat(receipt.getItems(), hasSize(1));
        assertThat(receipt.getItems().get(0).getId(), equalTo("B1"));
        assertThat(receipt.getItems().get(0).getQuantity(), equalTo(2));

        // Stock decrecido
        assertEquals(8, board.getStock());
    }

    @Test
    void processOrderRentalShouldApplyDepositAndDecreaseStock() {
        cart.addItem(board, 1); // 500
        OrderReceipt receipt = service.processOrder(cart, true);

        // Depósito 10% de 500 = 50.00
        assertThat(receipt.isRental(), is(true));
        assertThat(receipt.getSubtotal(), comparesEqualTo(new BigDecimal("500.00")));
        assertThat(receipt.getDeposit(), comparesEqualTo(new BigDecimal("50.00")));
        assertThat(receipt.getTotal(), comparesEqualTo(new BigDecimal("550.00")));
        assertEquals(9, board.getStock());
    }
}
