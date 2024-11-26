package net.ddns.sbapiserver.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "manufacturer_img")
        private String manufacturerImg;

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

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "created_at")
        private Timestamp createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "updated_at")
        private Timestamp updatedAt;

        public static Result of(Manufacturers manufacturers){
            return Result.builder()
                    .manufacturerId(manufacturers.getManufacturerId())
                    .manufacturerName(manufacturers.getManufacturerName())
                    .manufacturerImg(manufacturers.getManufacturerImg())
                    .createdAt(manufacturers.getCreatedAt())
                    .updatedAt(manufacturers.getUpdatedAt())
                    .build();
        }

        public static List<Result> of(List<Manufacturers> manufacturers){
            return manufacturers.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
