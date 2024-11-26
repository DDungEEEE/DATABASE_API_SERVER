package net.ddns.sbapiserver.domain.entity.basket;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;

@Table(name = "star_basket",
    uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "product_id"}),
    indexes = @Index(name = "idx_client_product", columnList = "client_id, product_id"))
@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarBasket {

    @Id @Column(name = "star_basket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer starBasketId;

    @With
    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne
    private Clients clients;

    @With
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Products products;
}
