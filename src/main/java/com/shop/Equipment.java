package com.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Equipment representa un producto del catálogo de la tienda de windsurf.
 * - Campos inmutables: id, name, price, type, rentalEligible
 * - Campo mutable: stock
 * - Valida entradas al construir.
 */
public class Equipment {
    // Identificador único del equipo
    private final String id; // inmutable
    // Nombre legible del equipo
    private final String name; // inmutable
    // Precio unitario
    private final BigDecimal price; // inmutable
    // Tipo de equipo
    private final EquipmentType type; // inmutable
    // Indica si es elegible para alquiler
    private final boolean rentalEligible; // inmutable

    // Stock disponible (mutable)
    private int stock;

    /**
     * Crea un Equipment válido.
     * Reglas:
     * - id y name no nulos ni vacíos
     * - price no nulo y >= 0
     * - stock >= 0
     * - type no nulo
     */
    public Equipment(String id, String name, BigDecimal price, int stock, EquipmentType type, boolean rentalEligible) {
        // Validar id
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        // Validar nombre
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        // Validar precio
        if (price == null) {
            throw new IllegalArgumentException("price must not be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        // Normalizar precio a escala 2 (moneda)
        this.price = price.setScale(2, RoundingMode.HALF_UP);

        // Validar stock
        if (stock < 0) {
            throw new IllegalArgumentException("stock must be >= 0");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.type = type;
        this.rentalEligible = rentalEligible;
    }

    // Getters públicos (sin setters para los inmutables)
    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public EquipmentType getType() { return type; }
    public boolean isRentalEligible() { return rentalEligible; }
    public int getStock() { return stock; }

    /**
     * Disminuye el stock con validaciones claras.
     * Reglas:
     * - qty > 0
     * - qty <= stock actual
     * Lanza IllegalArgumentException con mensajes específicos si viola reglas.
     */
    public void decreaseStock(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (qty > stock) {
            throw new IllegalArgumentException("insufficient stock");
        }
        // Aplicar decremento
        this.stock -= qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return id.equals(equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", type=" + type +
                ", rentalEligible=" + rentalEligible +
                '}';
    }
}
