package net.ddns.sbapiserver.domain.entity.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;

import java.sql.Timestamp;

@Entity  @Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Products {

    @Schema(name = "product_id")
    @Id @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Schema(name = "product_name")
    @Column(name = "product_name")
    private String productName;

    @Schema(name = "product_price")
    @Column(name = "product_price")
    private int productPrice;

    @Schema(name = "product_status")
    @Column(name = "product_status")
    private String productStatus;

    @Schema(name="product_img")
    @Column(name="product_img")
    private String productImg;

    @Schema(name="product_type")
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

    @Schema(name="product_enroll_date")
    @Column(name = "product_enroll_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp productEnrollDate;

    @Schema(name="product_modify_date")
    @Column(name="product_modify_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp productModifyDate;

    @PrePersist
    protected void setProductDate(){
        if(productEnrollDate == null){
            productEnrollDate = new Timestamp(System.currentTimeMillis());
        }
        if(productModifyDate == null){
            productModifyDate = new Timestamp(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void updateProductModifyDate(){
        productModifyDate = new Timestamp(System.currentTimeMillis());
    }
}
