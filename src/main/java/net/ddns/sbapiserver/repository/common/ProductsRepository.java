package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.common.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

    Products findByProductId(int productId);
    List<Products> findByManufacturersManufacturerId(int manufacturerId);
}
