package com.group_cordillera.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_boleta", nullable = false, unique = true)
    private String codigoBoleta;

    @Column(name = "sucursal", nullable = false)
    private String sucursal;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false)
    private CanalVenta canal;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();

    // Setter personalizado para asegurar la integridad
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        if (detalles != null) {
            for (DetalleVenta detalle : detalles) {
                detalle.setVenta(this);
            }
        }
    }
}