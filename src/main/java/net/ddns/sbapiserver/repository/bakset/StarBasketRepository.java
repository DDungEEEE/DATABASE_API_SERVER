package net.ddns.sbapiserver.repository.bakset;

import net.ddns.sbapiserver.domain.entity.basket.StarBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarBasketRepository extends JpaRepository<StarBasket, Integer> {
    Optional<StarBasket> findByClientsClientIdAndProductsProductId(Integer clientId, Integer productId);
    List<StarBasket> findByClientsClientId(Integer clientId);

}
