package com.group_cordillera.inventory.controller;


import com.group_cordillera.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventory")
@Slf4j
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> verificarStock(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        log.info("[INVENTORY CONTROLLER] Verificando disponibilidad. Producto ID: {}, Cantidad solicitada: {}", productoId, cantidad);
        boolean tieneStock = inventoryService.validarStock(productoId, cantidad);

        return ResponseEntity.ok(tieneStock);
    }
}