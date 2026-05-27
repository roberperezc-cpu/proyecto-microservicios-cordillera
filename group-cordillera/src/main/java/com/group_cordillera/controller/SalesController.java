package com.group_cordillera.controller;

import com.group_cordillera.dto.VentaRequestDTO;
import com.group_cordillera.dto.DetalleVentaRequestDTO;
import com.group_cordillera.model.DetalleVenta;
import com.group_cordillera.model.Venta;
import com.group_cordillera.service.PostService;
import com.group_cordillera.service.SalesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/sales")
@CrossOrigin(origins = "*")
@Slf4j // <-- Agrega la anotación aquí también
public class SalesController {

    @Autowired
    private SalesService service;

    // Nota: El PostService lo conectaremos en el paso 4
    @Autowired
    private PostService postService;

    public List<Venta> listar() { return service.getVentas(); }

    @GetMapping
    public ResponseEntity<List<Venta>> listarDenuevo(){
        log.info("[CONTROLLER] Endpoint GET '/' invocado para listar todas las ventas.");
        List<Venta> registro = service.getVentas();

        if(registro.isEmpty()) {
            log.warn("[CONTROLLER] No se encontraron registros de ventas. Retornando HTTP 204.");
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(registro);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Long id){
        log.info("[CONTROLLER] Endpoint GET '/{}' invocado.", id);
        Optional<Venta> registro = service.getVenta(id);

        if (registro.isPresent()) {
            return ResponseEntity.ok(registro.get());
        } else {
            log.warn("[CONTROLLER] Venta con ID {} no encontrada. Retornando HTTP 404.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Venta> agregarVenta(@Valid @RequestBody VentaRequestDTO dto){
        log.info("[CONTROLLER] Recibida petición POST para ingestar boleta: {}", dto.getCodigoBoleta());
        Venta nuevaVenta = convertirDtoAEntidad(dto);
        Venta nuevoRegistro = service.saveVenta(nuevaVenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> editarRegistro(@Valid @RequestBody VentaRequestDTO dto, @PathVariable Long id){
        log.info("[CONTROLLER] Recibida petición PUT para modificar ID: {}", id);
        Optional<Venta> existe = service.getVenta(id);

        if (existe.isEmpty()) {
            log.warn("[CONTROLLER] Error al editar. El ID {} no existe.", id);
            return ResponseEntity.notFound().build();
        }

        Venta registro = convertirDtoAEntidad(dto);
        registro.setId(id);
        Venta actualizado = service.saveVenta(registro);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id){
        log.info("[CONTROLLER] Recibida petición DELETE para ID: {}", id);
        try {
            service.deleteVenta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            log.error("[CONTROLLER ERROR] Falló la eliminación del ID {}. Motivo: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/post")
    public ResponseEntity<Map<String, Object>>
    registroConPost(@PathVariable Long id) {

        Optional<Venta> registroOpt = service.getVenta(id);

        if (registroOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Venta registro = registroOpt.get();

        // Simulación del postService de tu proyecto antiguo
            postService.obtenerPost();


        Map<String, Object> respuesta = new HashMap<>();
        // Cambiamos getNombreCompleto() por getCodigoBoleta() que pertenece a Ventas
        respuesta.put("registro", registro.getCodigoBoleta());


        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/guardar")
    public ResponseEntity<Venta> guardar(@Valid @RequestBody VentaRequestDTO dto) {
        Venta nuevaVenta = convertirDtoAEntidad(dto);
        return ResponseEntity.ok(service.saveVenta(nuevaVenta));
    }

    // MÉTODOS DE CONVERSIÓN (DTO -> ENTIDAD)
    private Venta convertirDtoAEntidad(VentaRequestDTO dto) {
        Venta venta = new Venta();
        venta.setCodigoBoleta(dto.getCodigoBoleta());
        venta.setSucursal(dto.getSucursal());
        venta.setTotal(dto.getTotal());
        venta.setCanal(dto.getCanal());
        venta.setFechaVenta(LocalDateTime.now());

        if (dto.getDetalles() != null) {
            List<DetalleVenta> detallesEn = dto.getDetalles().stream()
                    .map(this::convertirDetalleDtoAEntidad)
                    .collect(Collectors.toList());
            venta.setDetalles(detallesEn); // Nuestro setter personalizado asocia la FK sola
        }
        return venta;
    }

    private DetalleVenta convertirDetalleDtoAEntidad(DetalleVentaRequestDTO dto) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProductoId(dto.getProductoId());
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        return detalle;
    }



}

