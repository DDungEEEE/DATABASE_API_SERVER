package net.ddns.sbapiserver.domain.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.common.Products;

@Table(name = "order_contents")
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderContents {

    @Id @Column(name = "order_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderContentId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Products products;

    @Column(name = "product_cnt")
    private int productCnt;
}
