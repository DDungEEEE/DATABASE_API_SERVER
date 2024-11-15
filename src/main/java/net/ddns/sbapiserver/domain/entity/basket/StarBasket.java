package net.ddns.sbapiserver.domain.entity.basket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;

@Table(name = "star_basket")
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarBasket {

    @Id @Column(name = "star_basket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer starBasketId;

    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @ManyToOne
    private Clients clients;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    private Products products;
}
