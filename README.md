# shop-testing

Proyecto Maven (Java 17) para un caso de estudio de testing unitario (JUnit 5 + Hamcrest) en el dominio de una tienda (shop) con carrito y validación de stock, con opción de alquiler.

## Estructura
- src/main/java/com/shop/
  - EquipmentType.java
  - Equipment.java
  - Cart.java
  - OrderReceipt.java
  - OrderService.java
- src/test/java/com/shop/
  - EquipmentTest.java
  - CartTest.java
  - OrderServiceTest.java
  - ParametrizedStockTest.java
  - AssumptionsTest.java
  - SuiteAllTests.java
- pom.xml
- INFORME_TDD_WINDSURF.md (informe TDD)

## Requisitos
- Java 17+
- Maven 3.8+

## Ejecutar pruebas
- Maven:
  - mvn -q -DskipTests=false test
  - o simplemente: mvn -q test
- IDE (IntelliJ/VS Code):
  - Abrir el proyecto y ejecutar las clases de prueba (botón "Run tests"), p.ej. `SuiteAllTests`.

## Variables de entorno opcionales (Assumptions)
- CI=true para ejecutar la prueba condicionada en `AssumptionsTest.onlyRunOnCI`.
- RUN_EXTRA=yes para habilitar `AssumptionsTest.conditionallyEnabledByEnv`.

## Crear un .zip del proyecto (sin ejecutarlo por ti)
- PowerShell (Windows):
  - Desde el directorio que contiene la carpeta del proyecto, ejecutar:
    - Compress-Archive -Path .\ -DestinationPath .\shop-testing.zip -Force
- Línea de comandos (si tienes `tar`/`zip`):
  - zip -r shop-testing.zip .
  - o bien: tar -a -c -f shop-testing.zip .
- Explorador de Windows:
  - Click derecho sobre la carpeta del proyecto → Enviar a → Carpeta comprimida (zip).

## Notas
- El porcentaje de depósito de alquiler está en `OrderService.RENTAL_DEPOSIT_RATE` (10%).
- Los importes monetarios usan BigDecimal y redondeo HALF_UP a 2 decimales.
