package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.common.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

    Optional<List<Products>> findByManufacturersManufacturerId(int manufacturerId);
    void deleteAllByManufacturersManufacturerId(int manufacturerId);
    @Modifying
    @Query("UPDATE Products p SET p.manufacturerSort = null WHERE p.manufacturerSort.manufacturerSortId = :manufacturerSortId")
    void setManufacturerSortToNull(@Param(value = "manufacturerSortId") int manufacturerSortId);
    List<Products> getProductsByManufacturerSortManufacturerSortId(int manufacturerSortId);
}
