package com.shop;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * OrderReceipt es un POJO inmutable con el resultado del procesamiento del pedido.
 * Incluye: status, items (id, qty), subtotal, deposit (si aplica), total y la bandera rental.
 */
public class OrderReceipt {
    // Siempre "OK" si se retornó el recibo (excepciones se lanzan ante errores)
    private final String status; // inmutable
    // Lista simple con id y quantity
    private final List<OrderItem> items; // inmutable
    // Subtotal monetario de los ítems
    private final BigDecimal subtotal; // inmutable
    // Depósito aplicado sólo en alquiler
    private final BigDecimal deposit; // inmutable (puede ser 0.00)
    // Total a pagar = subtotal + deposit (si aplica)
    private final BigDecimal total; // inmutable
    // Indica si el pedido se procesó como alquiler
    private final boolean rental; // inmutable

    /** Item de pedido (id, quantity). */
    public static class OrderItem {
        private final String id; // inmutable
        private final int quantity; // inmutable

        public OrderItem(String id, int quantity) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("item id must not be blank");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("item quantity must be > 0");
            }
            this.id = id;
            this.quantity = quantity;
        }
        public String getId() { return id; }
        public int getQuantity() { return quantity; }
    }

    public OrderReceipt(List<OrderItem> items, BigDecimal subtotal, BigDecimal deposit, BigDecimal total, boolean rental) {
        // Validaciones básicas de nulidad
        this.status = "OK";
        this.items = Collections.unmodifiableList(Objects.requireNonNull(items, "items must not be null"));
        this.subtotal = Objects.requireNonNull(subtotal, "subtotal must not be null");
        this.deposit = Objects.requireNonNull(deposit, "deposit must not be null");
        this.total = Objects.requireNonNull(total, "total must not be null");
        this.rental = rental;
    }

    public String getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDeposit() { return deposit; }
    public BigDecimal getTotal() { return total; }
    public boolean isRental() { return rental; }
}
