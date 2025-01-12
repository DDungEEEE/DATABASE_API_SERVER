package net.ddns.sbapiserver.service.basket;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.basket.StarBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.StarBasket;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.repository.bakset.StarBasketRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StarBasketService {
    private final StarBasketRepository starBasketRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    /**
     * 처음 등록할 때 -> clientId, productId 등록
     * 만약 clientId, productId로 검색시 -> 존재 -> 해당 인스턴스 삭제
     * @param create
     * @return
     */
    public StarBasketDto.Result createStartBasket(StarBasketDto.Create create){
        Clients clients = serviceErrorHelper.findClientsOrElseThrow404(create.getClientId());
        Products products = serviceErrorHelper.findProductsOrElseThrow404(create.getProductId());

        Optional<StarBasket> existingStarBasket = starBasketRepository.findByClientsClientIdAndProductsProductId(create.getClientId(), create.getProductId());

        if(existingStarBasket.isPresent()){
            deleteStarBasket(existingStarBasket.get().getStarBasketId());
            return null;
        }else {
            StarBasket starBasket = StarBasket.builder()
                    .clients(clients)
                    .products(products)
                    .build();

            StarBasket resultStarBasket = starBasketRepository.save(starBasket);
            return StarBasketDto.Result.of(resultStarBasket);
        }
    }


    @Transactional
    public void deleteStarBasket(int starBasketId){
        starBasketRepository.deleteById(starBasketId);
    }

    @Transactional(readOnly = true)
    public List<StarBasketDto.Result> getStarBasketList(int clientId){
        List<StarBasket> findStarBasketList = starBasketRepository.findByClientsClientId(clientId);
        return StarBasketDto.Result.of(findStarBasketList);
    }

}
