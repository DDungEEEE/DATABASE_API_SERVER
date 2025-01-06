package net.ddns.sbapiserver.service.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ProductDto;
import net.ddns.sbapiserver.domain.entity.common.*;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
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
    public Products addProduct(ProductDto.Create create){

        Products productEntity = create.asEntity(products ->
                products.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(create.getProductManufacturerId()))
                        .withStaffs(serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId()))
                        .withManufacturerSort(serviceErrorHelper.findManufacturerSortOrElseThrow404(create.getManufacturerSortId()))
        );

        return productsRepository.save(productEntity);
    }

    @Transactional
    public void deleteProduct(int productId){
        serviceErrorHelper.findProductsOrElseThrow404(productId);
        productsRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }

    @Transactional
    public Products updateProducts(ProductDto.Put put){
        Products findProduct = serviceErrorHelper.findProductsOrElseThrow404(put.getProductId());
        Staffs findStaff = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        Manufacturers findManufacturer = serviceErrorHelper.findManufacturerOrElseThrow404(put.getProductManufacturerId());
        ManufacturerSort findManufacturerSort = serviceErrorHelper.findManufacturerSortOrElseThrow404(put.getManufacturerSortId());
        Products updatedProduct = put.asPutEntity(
                findProduct.
                withManufacturers(findManufacturer).
                        withStaffs(findStaff).
                        withManufacturerSort(findManufacturerSort));
        return productsRepository.save(updatedProduct);
    }

    @Transactional(readOnly = true)
    public List<Products> findProductsByManufacturersId(int manufacturerId, int manufacturerSortId){
        QProducts qProducts = QProducts.products;
        if(manufacturerId == 0){
                return jpaQueryFactory.selectFrom(qProducts)
                        .where(qProducts.manufacturerSort.manufacturerSortId.eq(manufacturerId))
                        .fetch();

        }else{
            return jpaQueryFactory.selectFrom(qProducts)
                    .where(qProducts.manufacturers.manufacturerId.eq(manufacturerId),
                            qProducts.manufacturerSort.manufacturerSortId.eq(manufacturerSortId))
                    .fetch();
        }
    }

}
