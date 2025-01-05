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

        public ManufacturerSort asEntity(Function<? super ManufacturerSort, ManufacturerSort> init){
            return init.apply(
                    ManufacturerSort.builder()
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

        public ManufacturerSort asPutEntity(ManufacturerSort manufacturerSort){
            manufacturerSort.setSortName(sortName);
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

        public static Result of(ManufacturerSort manufacturerSort){
            return Result.builder()
                    .manufacturerSortId(manufacturerSort.getManufacturerSortId())
                    .manufacturerId(manufacturerSort.getManufacturers().getManufacturerId())
                    .sortName(manufacturerSort.getSortName())
                    .build();
        }

        public static List<Result> of(List<ManufacturerSort> manufacturerSorts){
            return manufacturerSorts.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
