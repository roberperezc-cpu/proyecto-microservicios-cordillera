package com.group_cordillera.service;

import com.group_cordillera.model.Venta;
import com.group_cordillera.repository.VentaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j // <-- Anotación de Lombok que habilita la variable 'log'
public class SalesService {

    @Autowired
    private com.group_cordillera.client.InventoryClient inventoryClient; // Inyectamos el cliente WebClient


    @Autowired
    private VentaRepository repository;

    public List<Venta> getVentas() {
        log.info("[SERVICE] Solicitando el listado completo de ventas consolidadas (POS/Ecommerce).");
        List<Venta> lista = repository.findAll();
        log.info("[SERVICE] Recuperados {} registros desde la base de datos.", lista.size());
        return lista;
    }

    public Optional<Venta> getVenta(Long id) {
        log.info("[SERVICE] Buscando venta por ID: {}", id);
        return repository.findById(id);
    }

    public Venta saveVenta(Venta venta) {
        log.info("[SERVICE] Validando existencias de productos vía WebClient antes de guardar...");

        // 1. Recorremos los productos del detalle que vienen del DTO
        if (venta.getDetalles() != null) {
            for (var detalle : venta.getDetalles()) {
                // Llama al microservicio de inventarios mediante WebClient
                boolean stockDisponible = inventoryClient.consultarStock(detalle.getProductoId(), detalle.getCantidad());

                // 2. Si el microservicio nos dice que no hay stock, frenamos la operación
                if (!stockDisponible) {
                    log.error("[SERVICE ERROR] Stock insuficiente para el Producto ID: {}. Transacción abortada.", detalle.getProductoId());
                    throw new IllegalArgumentException("No hay stock suficiente en tienda para el producto con ID: " + detalle.getProductoId());
                }
            }
        }

        // 3. Si todos los productos tienen stock, finalmente persistimos en MySQL (Como lo hacía tu método antiguo)
        log.info("[SERVICE] Todo en orden con el inventario. Procediendo a guardar en tb_ventas...");
        return repository.save(venta);
    }

    public Venta updateVenta(Long id, Venta venta) {
        log.info("[SERVICE] Solicitud de actualización para la venta con ID: {}", id);
        Optional<Venta> existe = getVenta(id);

        if (existe.isEmpty()) {
            log.error("[SERVICE ERROR] No se pudo actualizar. Venta con ID {} no existe en el sistema.", id);
            throw new RuntimeException("No se encontró la venta");
        } else {
            venta.setId(id);
            Venta actualizada = repository.save(venta);
            log.info("[SERVICE] Venta con ID {} actualizada correctamente.", id);
            return actualizada;
        }
    }

    public void deleteVenta(Long id) {
        log.info("[SERVICE] Solicitud para eliminar la venta con ID: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("[SERVICE] Venta con ID {} eliminada físicamente de MySQL.", id);
        } else {
            log.error("[SERVICE ERROR] Intento fallido de eliminación. ID {} no encontrado.", id);
            throw new RuntimeException("No se encontró la venta");
        }
    }
}