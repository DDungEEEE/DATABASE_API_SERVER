package net.ddns.sbapiserver.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
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
        private BigDecimal productPrice;

        @Pattern(regexp = "주문 가능|주문 불가능\\(재고\\)|주문 불가능\\(기타\\)",
                message = "product_status는 '주문가능', '주문 불가능(재고)', '주문 불가능(기타)'만 입력 가능합니다.")
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

        @Schema(name = "manufacturer_sort_id")
        private int manufacturerSortId;

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
        private BigDecimal productPrice;

        @Pattern(regexp = "주문 가능|주문 불가능\\(재고\\)|주문 불가능\\(기타\\)",
                message = "product_status는 '주문 가능', '주문 불가능(재고)', '주문 불가능(기타)'만 입력 가능합니다.")
        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name="product_img")
        private String productImg;

        @Schema(name="product_type")
        private String productType;

        @Schema(name = "manufacturer_sort_id")
        private int manufacturerSortId;

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
    class Result {

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_price")
        private BigDecimal productPrice;

        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name = "product_img")
        private String productImg;

        @Schema(name = "product_type")
        private String productType;

        @Schema(name = "product_manufacturer_id")
        private Integer productManufacturerId;

        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "manufacturer_sort_id")
        private Integer manufacturerSortId;

        @Schema(name = "staff_id")
        private Integer staffId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "product_enroll_date")
        private Timestamp productEnrollDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "product_modify_date")
        private Timestamp productModifyDate;

        public static Result of(Products products) {
            return Result.builder()
                    .productName(products.getProductName())
                    .productId(products.getProductId())
                    .productImg(products.getProductImg())
                    .productPrice(products.getProductPrice())
                    .productStatus(products.getProductStatus())
                    .productType(products.getProductType())
                    .manufacturerSortId(checkManufacturerSortIsNull(products.getManufacturerSort()))
                    .productManufacturerId(checkManufacturerIsNull(products.getManufacturers()))
                    .manufacturerName(getManufacturerName(products.getManufacturers()))
                    .staffId(checkStaffIsNull(products.getStaffs()))
                    .productEnrollDate(products.getProductEnrollDate())
                    .productModifyDate(products.getProductModifyDate())
                    .build();
        }

        public static List<Result> of(List<Products> products) {
            return products.stream().map(Result::of)
                    .collect(Collectors.toList());
        }

        public static String getManufacturerName(Manufacturers manufacturers){
            if(manufacturers != null) {
                return manufacturers.getManufacturerName();
            }
            return null;
        }

        // 아직 데이터가 정리되기 전이어서 null 일경우 NullPointException 방지
        public static Integer checkStaffIsNull(Staffs staffs){
            if (staffs == null){
                return null;
            }else{
                return staffs.getStaffId();
            }
        }

        public static Integer checkManufacturerSortIsNull(ManufacturerSort manufacturerSort){
            if(manufacturerSort == null){
                return null;
            }else{
                return manufacturerSort.getManufacturerSortId();
            }
        }
        public static Integer checkManufacturerIsNull(Manufacturers manufacturers){
            if(manufacturers == null){
                return null;
            }else {
                return manufacturers.getManufacturerId();
            }
        }

    }

}
