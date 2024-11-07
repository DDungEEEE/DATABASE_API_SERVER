package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturersRepository extends JpaRepository<Manufacturers, Integer> {

}
