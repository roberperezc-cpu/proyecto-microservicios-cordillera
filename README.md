

## Integrantes del Equipo
* [Roberto Perez]
* [Pablo Acuña]
* [Vicente Gallardo]

---

## Descripción del Proyecto
Este proyecto consiste en una solución de software empresarial para la cadena **Group Cordillera**, diseñada bajo un enfoque arquitectónico de **Microservicios**. El sistema implementa una infraestructura distribuida, escalable y tolerante a fallos que centraliza las operaciones críticas del negocio, incluyendo la gestión transaccional de ventas, control de inventarios, administración de clientes, logística de despachos y flujos financieros.

---

## Arquitectura y Componentes Implementados

El ecosistema está compuesto por **10 microservicios** desacoplados, organizados de la siguiente manera:

### I. Componentes de Infraestructura Core
* **`discovery-server` (Puerto 8761):** Servidor de descubrimiento basado en **Spring Cloud Netflix Eureka**. Actúa como el registro centralizado para la localización dinámica de todas las instancias del sistema.
* **`api-gateway` (Puerto 8080):** Puerta de entrada única construida con **Spring Cloud Gateway**. Centraliza la recepción de solicitudes HTTP del exterior y gestiona el enrutamiento inteligente hacia los servicios correspondientes.

### II. Microservicios de Negocio y Operación
* **`group-cordillera` (Puerto 8081 | `/api/v1/sales`):** Módulo crítico transaccional encargado de la emisión de boletas, cálculo de totales de ventas, segregación por canales de atención (POS/Web) y procesamiento de detalles de venta.
* **`inventory-service` (Puerto 8082 | `/api/v1/products`):** Administra el catálogo maestro de productos, códigos de barra, listas de precios y control físico de stock.
* **`customer-service` (Puerto 8083 | `/api/v1/customers`):** Gestiona el registro de clientes, validación de identificadores (RUT) y perfiles de contacto.
* **`employee-service` (Puerto 8084 | `/api/v1/employees`):** Controla el personal operativo, asignación de cargos y roles por sucursal.
* **`payment-service` (Puerto 8085 | `/api/v1/payments`):** Procesa los métodos de pago (Efectivo, Tarjeta, Transferencia) y audita los estados de las transacciones.
* **`supplier-service` (Puerto 8086 | `/api/v1/suppliers`):** Administra la cadena de proveedores y abastecimiento de inventarios.
* **`delivery-service` (Puerto 8087 | `/api/v1/deliveries`):** Monitorea la logística de despachos y flujos de estados de envío.
* **`discount-service` (Puerto 8088 | `/api/v1/discounts`):** Aplica lógicas comerciales para campañas de descuentos y cupones de rebaja porcentual.

---

## Tecnologías Utilizadas
* **Backend:** Java 17 / Spring Boot 3.x
* **Ecosistema Cloud:** Spring Cloud Gateway, Netflix Eureka Server
* **Persistencia:** Hibernate / Spring Data JPA
* **Base de Datos:** MySQL (con soporte de Flyway para migraciones en módulos transaccionales críticos)
* **Utilidades:** Lombok, Maven

---

## Pasos para Ejecutar el Proyecto

Para levantar el ecosistema completo en un entorno local, siga estrictamente el siguiente orden de inicialización:

### Prerrequisitos
1. Tener instalado **Java 17** y **Maven**.
2. Contar con un motor **MySQL** activo (vía XAMPP/phpMyAdmin) escuchando en el puerto `3306`.
3. Crear la base de datos global (o las bases correspondientes): `group_cordillera`.

### Orden de Encendido en el IDE (IntelliJ IDEA / Eclipse)
1. **`discovery-server`**: Iniciar este servicio primero y esperar a que la interfaz de Eureka esté disponible en `http://localhost:8761`.
2. **`api-gateway`**: Inicializar para habilitar la puerta de enlace en el puerto `8080`.
3. **Microservicios de Negocio**: Iniciar de manera independiente los servicios (`group-cordillera`, `inventory-service`, `customer-service`, etc.). Verifique en el dashboard de Eureka que los 8 servicios de negocio se registren correctamente de forma automática.

### Consumo de Endpoints (Vía API Gateway)
Todas las consultas externas deben direccionarse única y exclusivamente a través del puerto del Gateway (`8080`). Ejemplos:
* **Obtener Ventas:** `GET http://localhost:8080/api/v1/sales`
* **Obtener Catálogo:** `GET http://localhost:8080/api/v1/products`
