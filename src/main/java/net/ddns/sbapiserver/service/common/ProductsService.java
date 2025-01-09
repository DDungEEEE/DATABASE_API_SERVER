package net.ddns.sbapiserver.service.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.common.ProductDto;
import net.ddns.sbapiserver.domain.entity.common.*;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public ProductDto.Result addProduct(ProductDto.Create create){

        Products productEntity = create.asEntity(products ->
                products.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(create.getProductManufacturerId()))
                        .withStaffs(serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId()))
                        .withManufacturerSort(serviceErrorHelper.findManufacturerSortOrElseThrow404(create.getManufacturerSortId()))
        );

        return ProductDto.Result.of(productsRepository.save(productEntity));
    }

    @Transactional
    public void deleteProduct(int productId){
        serviceErrorHelper.findProductsOrElseThrow404(productId);
        productsRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Result> getAllProducts(){
        List<Products> allProducts = productsRepository.findAll();
//        List<Products> sortedProducts = allProducts.stream().sorted(Comparator.comparing(Products::getProductName).reversed()).collect(Collectors.toList());

        return ProductDto.Result.of(allProducts);
    }

    @Transactional
    public ProductDto.Result updateProducts(ProductDto.Put put){
        Products findProduct = serviceErrorHelper.findProductsOrElseThrow404(put.getProductId());
        Staffs findStaff = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        Manufacturers findManufacturer = serviceErrorHelper.findManufacturerOrElseThrow404(put.getProductManufacturerId());
        ManufacturerSort findManufacturerSort = serviceErrorHelper.findManufacturerSortOrElseThrow404(put.getManufacturerSortId());
        Products updatedProduct = put.asPutEntity(
                findProduct.
                withManufacturers(findManufacturer).
                        withStaffs(findStaff).
                        withManufacturerSort(findManufacturerSort));
        return ProductDto.Result.of(productsRepository.save(updatedProduct));
    }


    // 제조사 id로 상품 불러오기
    @Transactional(readOnly = true)
    public List<ProductDto.Result> findProductsByManufacturersId(int manufacturerId){
        ErrorCode productNotFoundError = ErrorCode.PRODUCT_NOT_FOUND_ERROR;
        List<Products> products = productsRepository.findByManufacturersManufacturerId(manufacturerId).orElseThrow(
                () -> new BusinessException(productNotFoundError, productNotFoundError.getReason()));
        return ProductDto.Result.of(products);
    }

    //제조사id, 제품군 id로 상품 불러오기
    @Transactional(readOnly = true)
    public List<ProductDto.Result> findProductsByManufacturerAndSortId(int manufacturerId, int manufacturerSortId){
        QProducts qProducts = QProducts.products;

        List<Products> findProducts = jpaQueryFactory.selectFrom(qProducts)
                .where(qProducts.manufacturers.manufacturerId.eq(manufacturerId),
                        qProducts.manufacturerSort.manufacturerSortId.eq(manufacturerSortId))
                .fetch();

        Collections.sort(findProducts, (product1, product2) -> {
            int num1 = extractNumber(product1.getProductName());
            int num2 = extractNumber(product2.getProductName());
            return Integer.compare(num1, num2);
        });

        Collections.reverse(findProducts);
        
        return ProductDto.Result.of(findProducts);
    }

    protected int extractNumber(String str) {
        // "\\d+" 정규식 패턴을 사용하여 숫자를 찾는다.
        Matcher matcher = Pattern.compile("\\d+").matcher(str);

        // 숫자가 하나라도 발견되면
        StringBuilder fullNumber = new StringBuilder();
        if (matcher.find()) {
            // 첫 번째로 발견된 숫자를 문자열로 가져오고, 이를 정수로 변환하여 반환
            fullNumber.append(matcher.group());
        }

        // 숫자가 없으면 0을 반환
        return !fullNumber.isEmpty() ?Integer.parseInt(fullNumber.toString()) : 0;
    }
}
