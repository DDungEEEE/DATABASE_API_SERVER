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
import net.ddns.sbapiserver.service.basket.StarBasketService;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
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
        // 제품군 -> key -> 상품 List -> value Map 생성
        Map<Integer, List<Products>> groupedProducts = groupProductsByManufacturerSortId(products);
        //제품군 id 별로 분류 -> productList 값들을 이름 -> 숫자추출 정렬
        groupedProducts.forEach((manufacturerSortId, productList) -> sortProductsByName(productList));
        //다시 List로 합침
        List<Products> mergedProducts = groupedProducts.values().stream().flatMap(Collection::stream).toList();
        //제품군 order id로 정렬
        List<Products> resultProducts = sortByManufacturerSortOrder(mergedProducts);
        return ProductDto.Result.of(resultProducts);
    }

    //제조사id, 제품군 id로 상품 불러오기
    @Transactional(readOnly = true)
    public List<ProductDto.Result> findProductsByManufacturerAndSortId(int manufacturerId, int manufacturerSortId){
        QProducts qProducts = QProducts.products;

        List<Products> findProducts = jpaQueryFactory.selectFrom(qProducts)
                .where(qProducts.manufacturers.manufacturerId.eq(manufacturerId),
                        qProducts.manufacturerSort.manufacturerSortId.eq(manufacturerSortId))
                .fetch();


        sortProductsByName(findProducts);
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

    protected void sortProductsByName(List<Products> products) {
        products.sort((p1, p2) -> {
            int num1 = extractNumber(p1.getProductName());
            int num2 = extractNumber(p2.getProductName());
            return Integer.compare(num1, num2);
        });
    }


    protected Map<Integer, List<Products>> groupProductsByManufacturerSortId(List<Products> productsList){
        return productsList.stream().collect(Collectors.groupingBy(product ->
                product.getManufacturerSort() != null
                        ? product.getManufacturerSort().getManufacturerSortId() : 0
        ));
    }

    protected List<Products> sortByManufacturerSortOrder(List<Products> products) {
        return products.stream()
                .sorted(Comparator.comparing(product ->
                        product.getManufacturerSort() != null ? product.getManufacturerSort().getManufacturerSortOrder() : 0
                ))
                .toList();
    }
}
