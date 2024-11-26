package net.ddns.sbapiserver.service.basket;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.basket.MarketBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.repository.bakset.MarketBasketRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketBasketService {

    private final MarketBasketRepository marketBasketRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    public List<MarketBasketDto.Result> saveMarketBasket(List<MarketBasketDto.Create> creates){


        List<MarketBasket> marketBasketList = creates.stream()
                .map(create -> {
                    Clients clients = serviceErrorHelper.findClientsOrElseThrow404(create.getClientId());
                    Products products = serviceErrorHelper.findProductsOrElseThrow404(create.getProductId());
                    return create.asEntity(marketBasket ->
                            marketBasket.withProducts(products).withClients(clients));
                }).collect(Collectors.toList());

        List<MarketBasket> savedMarketbaksetList = marketBasketRepository.saveAll(marketBasketList);
        return MarketBasketDto.Result.of(savedMarketbaksetList);
    }

    public List<MarketBasketDto.Result> getMarketBasketList(int clientId){
        serviceErrorHelper.findClientsOrElseThrow404(clientId);

        List<MarketBasket> marketBaskets = marketBasketRepository.findMarketBasketsByClientsClientId(clientId);
        return MarketBasketDto.Result.of(marketBaskets);
    }

    @Transactional
    public void deleteByClientId(int clientId){
        serviceErrorHelper.findClientsOrElseThrow404(clientId);

        marketBasketRepository.deleteByClientsClientId(clientId);
    }
}
