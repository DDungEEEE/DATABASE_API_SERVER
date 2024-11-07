package net.ddns.sbapiserver.service.basket;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.basket.MarketBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.repository.bakset.MarketBasketRepository;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketBasketService {

    private final MarketBasketRepository marketBasketRepository;
    private final ProductsRepository productsRepository;
    private final ClientRepository clientRepository;

    private List<MarketBasket> saveMarketBasket(List<MarketBasketDto.Create> creates){
        List<MarketBasket> marketBaskets = new ArrayList<>();

        for(MarketBasketDto.Create create : creates){
            Clients clients = clientRepository.findById(create.getClientId()).get();
            Products product = productsRepository.findByProductId(create.getProductId());
            MarketBasket marketBasket = create.asEntity(init ->
                    init.withClients(clients)
                            .withProducts(product));

            marketBaskets.add(marketBasket);
        }
        return marketBasketRepository.saveAll(marketBaskets);
    }
}
