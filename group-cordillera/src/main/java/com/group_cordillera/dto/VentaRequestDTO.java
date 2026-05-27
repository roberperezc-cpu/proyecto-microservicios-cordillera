package com.group_cordillera.dto;

import com.group_cordillera.model.CanalVenta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {

    @NotBlank(message = "El código de boleta no puede estar vacío")
    private String codigoBoleta;

    @NotBlank(message = "La sucursal de origen es obligatoria")
    private String sucursal;

    @NotNull(message = "El total de la venta es obligatorio")
    @Positive(message = "El monto total de la venta debe ser un número positivo")
    private BigDecimal total;

    @NotNull(message = "El canal de venta es obligatorio (POS o ECOMMERCE)")
    private CanalVenta canal;

    @NotEmpty(message = "La venta debe contener al menos un producto en el detalle")
    @Valid // Crucial: Activa la validación en cascada para la lista de detalles
    private List<DetalleVentaRequestDTO> detalles;
}
