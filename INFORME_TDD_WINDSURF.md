# Informe TDD – Shop (Testing Unitario con JUnit 5)

## Resumen de TDD y ventajas de escribir pruebas primero
El Desarrollo Guiado por Pruebas (TDD) sigue el ciclo Red → Green → Refactor. Primero escribimos una prueba que falla (Red), luego implementamos el mínimo necesario para que pase (Green), y finalmente refactorizamos con seguridad (Refactor), apoyados por el suite de pruebas. Las ventajas clave:
- Diseño más simple y orientado al comportamiento.
- Feedback rápido y reducción de retrabajo.
- Mayor confianza para refactorizar.
- Documentación viva del sistema a través de las pruebas.

## Dos ciclos Red → Green → Refactor (ejemplos)
1) Validación de stock insuficiente
- Red: prueba en `OrderServiceTest.processOrderShouldFailWhenInsufficientStock` que intenta comprar 3 unidades cuando solo hay 2 en stock. La prueba falla si el servicio no valida correctamente.
- Green: implementamos en `OrderService.processOrder` la validación `qty > eq.getStock()` → `IllegalArgumentException`.
- Refactor: centralizamos la disminución de stock tras pasar todas las validaciones y normalizamos redondeos con `HALF_UP` a 2 decimales.

2) Depósito en alquiler (10%)
- Red: prueba en `OrderServiceTest.processOrderRentalShouldApplyDepositAndDecreaseStock` que espera depósito del 10% y total = subtotal + depósito.
- Green: agregamos constante `RENTAL_DEPOSIT_RATE = 0.10` y cálculo con `setScale(2, HALF_UP)`.
- Refactor: extraemos constantes, homogeneizamos formatos monetarios con `BigDecimal` a escala 2 y validamos que el recibo sea inmutable.

## Diseño y decisiones
- BigDecimal: para operaciones monetarias exactas evitando problemas de precisión de `double`. Redondeo `RoundingMode.HALF_UP` y escala 2.
- Excepciones específicas: usamos `IllegalArgumentException` con mensajes claros. En un sistema real se podrían introducir excepciones de dominio propias para mayor granularidad.
- Depósito de alquiler: definido en `OrderService.RENTAL_DEPOSIT_RATE` (10%). Facilita cambios de política sin tocar la lógica.
- Inmutabilidad: `Equipment` tiene campos inmutables salvo `stock`; `OrderReceipt` y `OrderItem` son inmutables para seguridad y facilidad de testeo.
- Catálogo en memoria: `Map<String, Equipment>` simple para el caso de estudio; en un sistema real vendría de una capa de persistencia.

## Cobertura de casos borde y cómo los tests los garantizan
- Carrito vacío: `OrderServiceTest.processOrderShouldFailWhenCartEmpty`.
- Equipo inexistente: `OrderServiceTest.processOrderShouldFailWhenEquipmentMissing`.
- Stock insuficiente: `OrderServiceTest.processOrderShouldFailWhenInsufficientStock`.
- Alquiler no elegible: `OrderServiceTest.processOrderShouldFailWhenRentalNotEligible`.
- Parametrizados decreaseStock: `ParametrizedStockTest` con cantidades válidas (1,2,5) e inválidas (0,-1,-5).
- Validaciones de constructor y `addItem`: `EquipmentTest` y `CartTest` cubren price >= 0, stock >= 0, name no vacío, qty > 0, acumulación por id, etc.
- Assumptions: `AssumptionsTest` demuestra cómo condicionar la ejecución según variables de entorno (p.ej. CI=true).
- Hamcrest: usado en `OrderServiceTest` para aserciones expresivas de números y cadenas.

## Ejecución
- Maven (desde la raíz del proyecto):
  - Compilar y ejecutar pruebas: `mvn -q -DskipTests=false test`
  - También funciona con: `mvn -q test`
- IntelliJ IDEA / VS Code:
  - Abrir el proyecto Maven.
  - Navegar a `src/test/java/com/shop/SuiteAllTests.java` y click en “Run”.
  - O bien ejecutar cada clase/prueba con el botón “Run tests”.

## Notas de implementación relevantes
- Depósito: `OrderService.RENTAL_DEPOSIT_RATE = 0.10`.
- Redondeo monetario: `setScale(2, RoundingMode.HALF_UP)` en subtotal, depósito y total.
- Recibo: `OrderReceipt` con `status="OK"`, `items` inmutables y getters; incluye `rental` para indicar compra vs. alquiler.
- Comentarios en el código: se añadieron comentarios aclaratorios línea a línea en puntos clave.

## Reflexión final
Aprendizajes y trade-offs: TDD obligó a definir la API y los casos borde antes de implementar, lo cual redujo retrabajo y facilitó refactors (por ejemplo, normalizar redondeos y mover el decremento de stock tras todas las validaciones). Riesgos como flaky tests se mitigan con fixtures simples, datos mínimos y evitando dependencias externas; en el futuro, de ser necesario, se introducirían dobles de prueba para aislar componentes.
