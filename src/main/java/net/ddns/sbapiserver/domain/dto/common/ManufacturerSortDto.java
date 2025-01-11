package net.ddns.sbapiserver.domain.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ManufacturerSortDto {

    @Data @Schema(name = "ManufacturerSortCreate")
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class Create{
        @Schema(name = "manufacturer_id")
        private int manufacturerId;
        @Schema(name = "sort_name")
        private String sortName;

        @Schema(name = "manufacturer_sort_order")
        private int manufacturerSortOrder;

        public ManufacturerSort asEntity(Function<? super ManufacturerSort, ManufacturerSort> init){
            return init.apply(
                    ManufacturerSort.builder()
                            .manufacturerSortOrder(manufacturerSortOrder)
                            .sortName(sortName)
                            .build()
            );
        }
    }

    @Data @Schema(name = "ManufacturerSortPut")
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class Put{
        @Schema(name = "manufacturer_sort_id")
        private int manufacturerSortId;

        @Schema(name = "manufacturer_id")
        private int manufacturerId;

        @Schema(name = "sort_name")
        private String sortName;

        @Schema(name = "manufacturer_sort_order")
        private int manufacturerSortOrder;

        public ManufacturerSort asPutEntity(ManufacturerSort manufacturerSort){
            manufacturerSort.setSortName(sortName);
            manufacturerSort.setManufacturerSortOrder(manufacturerSortOrder);
            return manufacturerSort;
        }
    }

    @Data @Schema(name = "ManufacturerSortResult")
    @Builder
    class Result{
        @Schema(name = "manufacturer_sort_id")
        private int manufacturerSortId;

        @Schema(name = "manufacturer_id")
        private int manufacturerId;

        @Schema(name = "sort_name")
        private String sortName;

        @Schema(name = "manufacturer_sort_order")
        private int manufacturerSortOrder;

        public static Result of(ManufacturerSort manufacturerSort){
            return Result.builder()
                    .manufacturerSortId(manufacturerSort.getManufacturerSortId())
                    .manufacturerId(manufacturerSort.getManufacturers().getManufacturerId())
                    .sortName(manufacturerSort.getSortName())
                    .manufacturerSortOrder(manufacturerSort.getManufacturerSortOrder())
                    .build();
        }

        public static List<Result> of(List<ManufacturerSort> manufacturerSorts){
            return manufacturerSorts.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
