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

    //상품의 제조사
    @With
    @JoinColumn(name = "product_manufacturer_id", referencedColumnName = "manufacturer_id")
    @ManyToOne
    private Manufacturers manufacturers;

    //상품의 제조사 ->상세 구분
    @With
    @ManyToOne
    @JoinColumn(name = "manufacturer_sort_id")
    private ManufacturerSort manufacturerSort;

    @With
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staffs staffs;

    @Column(name = "product_enroll_date", nullable = false, updatable = false)
    private Timestamp productEnrollDate;

    @Column(name="product_modify_date", nullable = false)
    private Timestamp productModifyDate;

    @PreUpdate
    protected void setUpdateProductModifyDate(){
        this.productModifyDate = new Timestamp(System.currentTimeMillis());
    }
    @PrePersist
    protected void setDefaultProductPriceAndModifyDate(){
            if(this.productPrice == null){
                this.productPrice = new BigDecimal("0.00");
            }
    }

}
