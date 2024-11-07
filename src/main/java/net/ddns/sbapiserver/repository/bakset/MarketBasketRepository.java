package net.ddns.sbapiserver.repository.bakset;

import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketBasketRepository extends JpaRepository<MarketBasket, Integer> {
}
