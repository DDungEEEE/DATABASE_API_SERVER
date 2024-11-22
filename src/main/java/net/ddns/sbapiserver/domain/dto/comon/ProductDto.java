package net.ddns.sbapiserver.domain.dto.comon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ProductDto {

    @Data
    @Schema(name = "ProductCreate")
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
    @Schema(name = "productPut")
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

    @Data
    @Schema(name = "ProductResult")
    @Builder
    class Result{

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_price")
        private int productPrice;

        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name="product_img")
        private String productImg;

        @Schema(name="product_type")
        private String productType;

        @Schema(name = "product_manufacturer_id")
        private Integer productManufacturerId;

        @Schema(name = "staff_id")
        private Integer staffId;

        @Schema(name = "product_enroll_date")
        private Timestamp productEnrollDate;

        @Schema(name = "product_modify_date")
        private Timestamp productModifyDate;

        public static Result of(Products products){
            return Result.builder()
                    .productName(products.getProductName())
                    .productId(products.getProductId())
                    .productImg(products.getProductImg())
                    .productPrice(products.getProductPrice())
                    .productStatus(products.getProductStatus())
                    .productType(products.getProductType())
                    .productManufacturerId(checkManufacturerIsNull(products.getManufacturers()))
                    .staffId(checkStaffIsNull(products.getStaffs()))
                    .productEnrollDate(products.getProductEnrollDate())
                    .productModifyDate(products.getProductModifyDate())
                    .build();
        }

        public static List<Result> of(List<Products> products){
           return products.stream().map(Result::of)
                    .collect(Collectors.toList());
        }

        protected static Integer checkStaffIsNull(Staffs staffs){
            if(staffs == null){
                return null;
            }
            return staffs.getStaffId();
        }

        protected static Integer checkManufacturerIsNull(Manufacturers manufacturers){
            if(manufacturers == null){
                return null;
            }
            return manufacturers.getManufacturerId();
        }
    }

}
