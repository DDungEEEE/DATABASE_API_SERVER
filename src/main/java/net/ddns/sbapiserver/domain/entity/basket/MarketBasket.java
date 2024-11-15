package net.ddns.sbapiserver.domain.entity.basket;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "market_basket")
public class MarketBasket {

    @Id @Column(name = "market_basket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer marketBasketId;

    @Column(name = "product_cnt")
    private int productCnt;

    @With
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Clients clients;

    @With
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Products products;
}
