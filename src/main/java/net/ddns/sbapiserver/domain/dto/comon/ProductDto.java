package net.ddns.sbapiserver.domain.dto.comon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.common.Products;

import java.util.function.Function;

public interface ProductDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Create{

        @Schema(name = "product_name")
        @NotNull(message = "상품 이름을 입력해주세요.")
        private String productName;

        @Schema(name = "product_price")
        @NotNull(message = "상품 가격을 입력해주세요.")
        @Digits(integer = 10, fraction = 2, message = "가격은 최대 10자리 숫자와 소수점 이하 2자리여야 합니다.")
        @DecimalMin(value = "0.00", message = "가격은 0.00 이상이어야 합니다.")
        private int productPrice;

        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name="product_img")
        private String productImg;

        @Schema(name="product_type")
        private String productType;


        @Schema(name = "product_manufacturer_id")
        private int productManufacturerId;

        @Schema(name = "staff_id")
        private int staffId;

        public Products asEntity(
                Function<? super Products, ? extends Products> init){
            return init.apply(
                    Products.builder()
                            .productName(productName)
                            .productPrice(productPrice)
                            .productStatus(productStatus)
                            .productImg(productImg)
                            .productType(productType)
                            .build());
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Put{

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "product_name")
        @NotNull(message = "상품 이름을 입력해주세요.")
        private String productName;

        @Schema(name = "product_price")
        @NotNull(message = "상품 가격을 입력해주세요.")
        @Digits(integer = 10, fraction = 2, message = "가격은 최대 10자리 숫자와 소수점 이하 2자리여야 합니다.")
        @DecimalMin(value = "0.00", message = "가격은 0.00 이상이어야 합니다.")
        private int productPrice;

        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name="product_img")
        private String productImg;

        @Schema(name="product_type")
        private String productType;


        @Schema(name = "product_manufacturer_id")
        private int productManufacturerId;

        @Schema(name = "staff_id")
        private int staffId;

        public Products asPutEntity(Products products){
            products.setProductName(productName);
            products.setProductPrice(productPrice);
            products.setProductStatus(productStatus);
            products.setProductImg(productImg);
            products.setProductType(productType);
            return products;
        }
    }

}
