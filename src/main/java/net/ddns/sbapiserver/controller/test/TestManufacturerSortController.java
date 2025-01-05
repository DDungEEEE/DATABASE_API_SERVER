package net.ddns.sbapiserver.controller.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerSortDto;
import net.ddns.sbapiserver.service.common.ManufacturerSortService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "제품군 컨트롤러")
@RequestMapping("/api/manufacturersort")
@RestController
@RequiredArgsConstructor
public class TestManufacturerSortController {
    private final ManufacturerSortService manufacturerSortService;

    @GetMapping
    public ResultResponse<List<ManufacturerSortDto.Result>> getAllSorts(){
        List<ManufacturerSortDto.Result> allManufacturerSort = manufacturerSortService.getAllManufacturerSort();
        return ResultResponse.<List<ManufacturerSortDto.Result>>successResponse()
                .result(allManufacturerSort)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @GetMapping("/{manufacturer_id}")
    public ResultResponse<List<ManufacturerSortDto.Result>> findManufacturerSort(@PathVariable(value = "manufacturer_id") int manufacturerId){
        List<ManufacturerSortDto.Result> findManufacturers = manufacturerSortService.findManufacturerSortByManufacturerId(manufacturerId);
        return ResultResponse.<List<ManufacturerSortDto.Result>>successResponse()
                .result(findManufacturers)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping
    public ResultResponse<ManufacturerSortDto.Result> createSort(@RequestBody ManufacturerSortDto.Create create){
        ManufacturerSortDto.Result createManufacturerSort = manufacturerSortService.createManufacturerSort(create);
        return ResultResponse.<ManufacturerSortDto.Result>successResponse()
                .result(createManufacturerSort)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @PutMapping
    public ResultResponse<ManufacturerSortDto.Result> updateSort(@RequestBody ManufacturerSortDto.Put put){
        ManufacturerSortDto.Result updateManufacturerSort = manufacturerSortService.updateManufacturerSort(put);
        return ResultResponse.<ManufacturerSortDto.Result>successResponse()
                .result(updateManufacturerSort)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @DeleteMapping("/{manufacturer_sort_id}")
    public ResultResponse<Void> deleteSort(@PathVariable("manufacturer_sort_id") int manufacturerSortId){
        manufacturerSortService.deleteManufacturerSortById(manufacturerSortId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}