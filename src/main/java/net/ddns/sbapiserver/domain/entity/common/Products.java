package net.ddns.sbapiserver.domain.entity.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity  @Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Products {

    @Id @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal productPrice;

    @Column(name = "product_status")
    private String productStatus;

    @Column(name="product_img")
    private String productImg;

    @Column(name="product_type")
    private String productType;

    @With
    @JoinColumn(name = "product_manufacturer_id", referencedColumnName = "manufacturer_id")
    @ManyToOne
    private Manufacturers manufacturers;

    @With
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staffs staffs;

    @CreationTimestamp
    @Column(name = "product_enroll_date", nullable = false, updatable = false)
    private Timestamp productEnrollDate;

    @UpdateTimestamp
    @Column(name="product_modify_date", nullable = false, updatable = false)
    private Timestamp productModifyDate;

    @PrePersist
    protected void setDefaultProductPriceAndModifyDate(){
            if(productPrice == null){
                productPrice = new BigDecimal("0.00");
            }
            productModifyDate = new Timestamp(System.currentTimeMillis());
    }

}
