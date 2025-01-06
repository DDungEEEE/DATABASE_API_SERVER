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

import java.util.List;

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
        return ProductDto.Result.of(productsRepository.findAll());
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
        return ProductDto.Result.of(findProducts);
    }
}
