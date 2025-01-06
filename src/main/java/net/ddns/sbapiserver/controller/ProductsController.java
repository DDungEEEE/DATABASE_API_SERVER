package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ProductDto;
import net.ddns.sbapiserver.domain.dto.common.ProductDto.Create;
import net.ddns.sbapiserver.domain.dto.common.ProductDto.Put;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.service.common.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 컨트롤러",description = "상품 조회, 삭제, 수정, 추가")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {

    private final ProductsService productsService;

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResultResponse<List<ProductDto.Result>> getProducts(){
        List<ProductDto.Result> allProducts = productsService.getAllProducts();

        return ResultResponse.<List<ProductDto.Result>>successResponse()
                .result(allProducts)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "상품 추가")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResultResponse<ProductDto.Result> create(@RequestBody @Valid Create create){
        ProductDto.Result product = productsService.addProduct(create);
        return ResultResponse.<ProductDto.Result>successResponse()
                .result(product)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "제조사로 상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{manufacturer_id}")
    ResultResponse<List<ProductDto.Result>> find(@PathVariable("manufacturer_id") int manufacturerId){
        List<ProductDto.Result> findProducts = productsService.findProductsByManufacturersId(manufacturerId);

        return ResultResponse.<List<ProductDto.Result>>successResponse()
                .result(findProducts)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "제조사, 상품군으로 상품들 조회")
    @GetMapping("/{manufacturer_id}/{manufacturer_sort_id}")
    ResultResponse<List<ProductDto.Result>> findByMacIdAndSortId(@PathVariable(value = "manufacturer_id") int manufacturerId, @PathVariable(value = "manufacturer_sort_id") int manufacturerSortId){
        List<ProductDto.Result> productsByManufacturerAndSortId = productsService.findProductsByManufacturerAndSortId(manufacturerId, manufacturerSortId);
        return ResultResponse.<List<ProductDto.Result>>successResponse()
                .result(productsByManufacturerAndSortId)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "상품 수정")
    @PutMapping
    ResultResponse<ProductDto.Result> updateProduct(@RequestBody Put put){
        ProductDto.Result updateProducts = productsService.updateProducts(put);
        return ResultResponse.<ProductDto.Result>successResponse()
                .result(updateProducts)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{product_id}")
    ResultResponse<Void> delete(@PathVariable("product_id") int productId){
        productsService.deleteProduct(productId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

}
