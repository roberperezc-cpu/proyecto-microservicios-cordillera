package com.group_cordillera.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryService {

    public boolean validarStock(Long productoId, Integer cantidad) {
        log.info("[INVENTORY SERVICE] Validando stock lógico para producto: {}", productoId);

        // Simulación: El producto ID 99 se quedó sin stock para probar el caso de error
        if (productoId == 99) {
            log.warn("[INVENTORY SERVICE] Producto 99 identificado sin existencias.");
            return false;
        }

        // Cualquier otro producto simulará que tiene stock de sobra
        return true;
    }
}