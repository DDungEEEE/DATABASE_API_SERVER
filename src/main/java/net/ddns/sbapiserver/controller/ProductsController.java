package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.comon.ProductDto;
import net.ddns.sbapiserver.domain.dto.comon.ProductDto.Create;
import net.ddns.sbapiserver.domain.dto.comon.ProductDto.Put;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.service.product.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/products")
public class ProductsController {

    private final ProductsService productsService;

    @Operation(summary = "상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResultResponse<List<ProductDto.Result>> getProducts(){
        List<Products> allProductList = productsService.getAllProducts();

        return ResultResponse.<List<ProductDto.Result>>dataResponse()
                .result(ProductDto.Result.of(allProductList))
                .resultCode(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "상품 추가")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResultResponse<Products> create(@RequestBody @Valid Create create){
        Products products = productsService.addProduct(create);
        return ResultResponse.<Products>dataResponse()
                .result(products)
                .resultCode(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "제조사로 상품 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{manufacturer_id}")
    ResultResponse<List<Products>> find(@PathVariable("manufacturer_id") int manufacturerId){
        List<Products> productsByManufacturersId = productsService.findProductsByManufacturersId(manufacturerId);

        return ResultResponse.<List<Products>>dataResponse()
                .result(productsByManufacturersId)
                .build();
    }

    @Operation(summary = "상품 수정")
    @PutMapping
    ResultResponse<Products> updateProduct(@RequestBody Put put){
        Products products = productsService.updateProducts(put);
        return ResultResponse.<Products>dataResponse()
                .result(products)
                .build();
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{product_id}")
    ResultResponse<Void> delete(@PathVariable("product_id") int productId){
        productsService.deleteProduct(productId);
        return ResultResponse.<Void>dataResponse()
                .resultCode(HttpStatus.NO_CONTENT)
                .resultMessage("삭제 완료되었습니다.")
                .build();
    }

}
