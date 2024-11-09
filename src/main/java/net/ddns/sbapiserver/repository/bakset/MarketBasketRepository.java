package net.ddns.sbapiserver.repository.bakset;

import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketBasketRepository extends JpaRepository<MarketBasket, Integer> {
    List<MarketBasket> findMarketBasketsByClientsClientId(Integer clientId);
    void deleteByClientsClientId(Integer clientId);
}
