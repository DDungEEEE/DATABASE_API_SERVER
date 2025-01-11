package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerSortRepository extends JpaRepository<ManufacturerSort, Integer> {
    List<ManufacturerSort> getManufacturerSortByManufacturersManufacturerId(int manufacturerId);
    void deleteAllByManufacturersManufacturerId(int manufacturerId);

}
