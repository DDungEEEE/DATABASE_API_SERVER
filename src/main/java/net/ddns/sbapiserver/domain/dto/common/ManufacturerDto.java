package net.ddns.sbapiserver.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public interface ManufacturerDto {

    @Schema(name = "manufacturerCreate")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Create{
        @NotNull(message = "제조사 이름은 필수입니다.")
        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "manufacturer_img")
        private String manufacturerImg;

        @NotBlank(message = "제조사 상태는 공백일 수 없습니다.")
        @Pattern(regexp = "활성|비활성", message = "manufacturer_order는 '활성', '비활성'만 입력 가능합니다.")
        @Schema(name = "manufacturer_status")
        private String manufacturerStatus;

        @Schema(name = "manufacturer_order")
        private Integer manufacturerOrder;

        @Schema(name = "staff_id")
        private int staffId;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "manufacturerPut")
    class Put{
        @NotNull(message = "제조사 아이디는 필수입니다.")
        @Schema(name = "manufacturer_id")
        private int manufacturerId;

        @NotBlank(message = "제조사 이름은 공백일 수 없습니다.")
        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "manufacturer_img")
        private String manufacturerImg;

        @NotNull(message = "제조사 상태는 공백일 수 없습니다.")
        @Schema(name = "manufacturer_status")
        private String manufacturerStatus;

        @Schema(name = "manufacturer_order")
        private int manufacturerOrder;

        @Schema(name = "staff_id")
        private int staffId;

        public Manufacturers asPutEntity(){
            return Manufacturers.builder()
                    .manufacturerId(manufacturerId)
                    .manufacturerName(manufacturerName)
                    .manufacturerImg(manufacturerImg)
                    .manufacturerStatus(manufacturerStatus)
                    .manufacturerOrder(manufacturerOrder)
                    .build();
        }
    }

    @Schema(name = "manufacturerResult")
    @Data
    @Builder
    class Result{
        @Schema(name = "manufacturer_id")
        private int manufacturerId;

        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "manufacturer_img")
        private String manufacturerImg;

        @Schema(name = "manufacturer_status")
        private String manufacturerStatus;

        @Schema(name = "manufacturer_order")
        private int manufacturerOrder;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "created_at")
        private Timestamp createdAt;

        @Schema(name = "staff_id")
        private int staffId;

        @Schema(name = "staff_name")
        private String staffName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "updated_at")
        private Timestamp updatedAt;

        public static Result of(Manufacturers manufacturers){
            return Result.builder()
                    .manufacturerId(manufacturers.getManufacturerId())
                    .manufacturerName(manufacturers.getManufacturerName())
                    .manufacturerImg(manufacturers.getManufacturerImg())
                    .manufacturerStatus(manufacturers.getManufacturerStatus())
                    .createdAt(manufacturers.getCreatedAt())
                    .updatedAt(manufacturers.getUpdatedAt())
                    .manufacturerOrder(manufacturers.getManufacturerOrder())
                    .staffId(manufacturers.getStaffs().getStaffId())
                    .staffName(manufacturers.getStaffs().getStaffName())
                    .build();
        }

        public static List<Result> of(List<Manufacturers> manufacturers){
            return manufacturers.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
