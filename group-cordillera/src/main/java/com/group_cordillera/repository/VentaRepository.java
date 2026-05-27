package com.group_cordillera.repository;

import com.group_cordillera.model.Venta;
import com.group_cordillera.model.CanalVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Permite segmentar datos por ubicación nacional [cite: 4]
    List<Venta> findBySucursal(String sucursal);

    // Permite filtrar datos según los softwares de origen (POS o Ecommerce) [cite: 7, 8]
    List<Venta> findByCanal(CanalVenta canal);
}