package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.service.common.ManufacturerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "제조사 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/manufacturer")
@RestController
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    public ResultResponse<List<ManufacturerDto.Result>> getManufacturer(){
        List<ManufacturerDto.Result> manufacturerList = manufacturerService.getManufacturerList();
        return ResultResponse.<List<ManufacturerDto.Result>>successResponse()
                .result(manufacturerList)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping
    public ResultResponse<ManufacturerDto.Result> createManufacturer(@RequestBody ManufacturerDto.Create create){
        ManufacturerDto.Result result = manufacturerService.CreateManufacturer(create);
        return ResultResponse.<ManufacturerDto.Result>successResponse()
                .result(result)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @DeleteMapping("/{manufacturer_id}")
    public ResultResponse<Void> deleteManufacturer(@PathVariable("manufacturer_id") int manufacturerId){
        manufacturerService.deleteManufacturerById(manufacturerId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
