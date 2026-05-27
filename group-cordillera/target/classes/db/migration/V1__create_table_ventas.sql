CREATE TABLE tb_ventas (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           codigo_boleta VARCHAR(100) NOT NULL UNIQUE,
                           sucursal VARCHAR(150) NOT NULL,
                           total DECIMAL(15, 2) NOT NULL,
                           canal VARCHAR(50) NOT NULL,
                           fecha_venta DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tb_detalle_ventas (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   venta_id BIGINT NOT NULL,
                                   producto_id BIGINT NOT NULL,
                                   cantidad INT NOT NULL,
                                   precio_unitario DECIMAL(15, 2) NOT NULL,
                                   CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES tb_ventas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;