package net.ddns.sbapiserver.domain.entity.order;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.common.Products;
import org.hibernate.annotations.Fetch;

@Table(name = "order_contents")
@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderContents {

    @Id @Column(name = "order_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderContentId;


    @ManyToOne @With
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Orders orders;

    @ManyToOne @With
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Products products;

    @Column(name = "product_cnt")
    private int productCnt;
}
