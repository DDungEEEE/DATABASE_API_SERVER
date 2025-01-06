package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.common.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

    Products findByProductId(int productId);
    Optional<List<Products>> findByManufacturersManufacturerId(int manufacturerId);
}
