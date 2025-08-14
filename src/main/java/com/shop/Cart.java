package com.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cart representa un carrito de compra/alquiler.
 * Mantiene un mapa de equipmentId -> quantity.
 */
public class Cart {
    // Se preserva el orden de inserción solo para facilitar lectura en recibos
    private final Map<String, Integer> items = new LinkedHashMap<>();

    /**
     * Agrega un item al carrito.
     * Reglas:
     * - Equipment no nulo
     * - qty > 0
     * - Acumula cantidad si ya existe el id
     */
    public void addItem(Equipment equipment, int qty) {
        if (equipment == null) {
            throw new IllegalArgumentException("equipment must not be null");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        // Acumular cantidades por id
        items.merge(equipment.getId(), qty, Integer::sum);
    }

    /** Devuelve una vista inmutable del mapa id->qty. */
    public Map<String, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /** Indica si el carrito está vacío. */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Estima el subtotal usando el catálogo provisto.
     * - Lanza IllegalArgumentException si falta algún id en el catálogo.
     */
    public BigDecimal estimateSubtotal(Map<String, Equipment> catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog must not be null");
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            String id = e.getKey();
            int qty = e.getValue();
            Equipment eq = catalog.get(id);
            if (eq == null) {
                throw new IllegalArgumentException("equipment not found in catalog: " + id);
            }
            // price * qty acumulado
            BigDecimal line = eq.getPrice().multiply(BigDecimal.valueOf(qty));
            subtotal = subtotal.add(line);
        }
        // Redondear a 2 decimales por consistencia monetaria
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
}
