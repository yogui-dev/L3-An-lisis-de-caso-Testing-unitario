package com.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * OrderService procesa pedidos (compra o alquiler) a partir de un carrito y un catálogo.
 * Reglas principales:
 * - Carrito no vacío
 * - Valida existencia y stock suficiente por ítem
 * - Si rental=true, valida que el equipo sea rentalEligible
 * - Calcula subtotal; si rental, aplica depósito fijo del 10% (HALF_UP, 2 decimales)
 * - Disminuye stock del catálogo (efecto real tras validar todo)
 * - Retorna OrderReceipt con status "OK"
 */
public class OrderService {
    // Depósito del 10% para alquileres
    public static final BigDecimal RENTAL_DEPOSIT_RATE = new BigDecimal("0.10");

    private final Map<String, Equipment> catalog; // referencia al catálogo

    public OrderService(Map<String, Equipment> catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog must not be null");
        }
        this.catalog = catalog;
    }

    /**
     * Procesa el pedido, validando reglas de negocio y actualizando stock.
     */
    public OrderReceipt processOrder(Cart cart, boolean rental) {
        // Validar carrito
        if (cart == null) {
            throw new IllegalArgumentException("cart must not be null");
        }
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("cart must not be empty");
        }

        // Pre-validación: existencia, elegibilidad (si rental) y stock
        List<OrderReceipt.OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            String id = e.getKey();
            int qty = e.getValue();

            Equipment eq = catalog.get(id);
            if (eq == null) {
                throw new IllegalArgumentException("equipment does not exist: " + id);
            }
            if (qty <= 0) {
                throw new IllegalArgumentException("quantity must be > 0 for item: " + id);
            }
            if (rental && !eq.isRentalEligible()) {
                throw new IllegalArgumentException("equipment not eligible for rental: " + id);
            }
            if (qty > eq.getStock()) {
                throw new IllegalArgumentException("insufficient stock for item: " + id);
            }

            // Acumular subtotal y preparar items del recibo
            BigDecimal line = eq.getPrice().multiply(BigDecimal.valueOf(qty));
            subtotal = subtotal.add(line);
            orderItems.add(new OrderReceipt.OrderItem(id, qty));
        }

        // Redondear subtotal a 2 decimales
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

        // Calcular depósito si rental
        BigDecimal deposit = BigDecimal.ZERO;
        if (rental) {
            deposit = subtotal.multiply(RENTAL_DEPOSIT_RATE).setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal total = subtotal.add(deposit).setScale(2, RoundingMode.HALF_UP);

        // Efectivizar decremento de stock (todas las validaciones pasaron)
        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            Equipment eq = Objects.requireNonNull(catalog.get(e.getKey()));
            eq.decreaseStock(e.getValue());
        }

        // Construir y retornar recibo
        return new OrderReceipt(orderItems, subtotal, deposit, total, rental);
    }
}
