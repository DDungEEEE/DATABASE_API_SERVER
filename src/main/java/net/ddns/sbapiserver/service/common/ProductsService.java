package net.ddns.sbapiserver.service.common;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.comon.ProductDto;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ManufacturersRepository manufacturersRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    public Products addProduct(ProductDto.Create create){

        Products productEntity = create.asEntity(products ->
                products.withManufacturers(manufacturersRepository.findById(create.getProductManufacturerId()).get())
                        .withStaffs(serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId())));

        return productsRepository.save(productEntity);
    }

    public void deleteProduct(int productId){
        serviceErrorHelper.findProductsOrElseThrow404(productId);
        productsRepository.deleteById(productId);
    }

    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }

    public Products updateProducts(ProductDto.Put put){
        Products findProduct = serviceErrorHelper.findProductsOrElseThrow404(put.getProductId());
        Staffs findStaff = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        Manufacturers findManufacturer = serviceErrorHelper.findManufacturerOrElseThrow404(put.getProductManufacturerId());

        Products updatedProduct = put.asPutEntity(findProduct.withManufacturers(findManufacturer).withStaffs(findStaff));
        return productsRepository.save(updatedProduct);
    }

    public List<Products> findProductsByManufacturersId(int manufacturerId){
        return productsRepository.findByManufacturersManufacturerId(manufacturerId);
    }

}
