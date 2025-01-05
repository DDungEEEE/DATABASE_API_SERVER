package net.ddns.sbapiserver.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ProductDto;
import net.ddns.sbapiserver.domain.dto.common.ProductDto.Create;
import net.ddns.sbapiserver.domain.dto.common.ProductDto.Put;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.service.common.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 컨트롤러",description = "상품 조회, 삭제, 수정, 추가")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class TestProductsController {

    private final ProductsService productsService;

    @Operation(summary = "상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResultResponse<List<ProductDto.Result>> getProducts(){
        List<Products> allProductList = productsService.getAllProducts();

        return ResultResponse.<List<ProductDto.Result>>successResponse()
                .result(ProductDto.Result.of(allProductList))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "상품 추가")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResultResponse<ProductDto.Result> create(@RequestBody @Valid Create create){
        Products products = productsService.addProduct(create);
        return ResultResponse.<ProductDto.Result>successResponse()
                .result(ProductDto.Result.of(products))
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @Operation(summary = "제조사로 상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{manufacturer_id}/{manufacturer_sort_id}")
    ResultResponse<List<Products>> find(@PathVariable("manufacturer_id") int manufacturerId, @PathVariable("manufacturer_sort_id") int manufacturerSortId){
        List<Products> productsByManufacturersId = productsService.findProductsByManufacturersId(manufacturerId, manufacturerSortId);

        return ResultResponse.<List<Products>>successResponse()
                .result(productsByManufacturersId)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "상품 수정")
    @PutMapping
    ResultResponse<Products> updateProduct(@RequestBody Put put){
        System.out.println(put.toString());
        Products products = productsService.updateProducts(put);

        return ResultResponse.<Products>successResponse()
                .result(products)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{product_id}")
    ResultResponse<Void> delete(@PathVariable("product_id") int productId){
        productsService.deleteProduct(productId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

}
