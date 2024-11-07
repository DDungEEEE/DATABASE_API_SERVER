package net.ddns.sbapiserver.service.product;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.comon.ProductDto;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final StaffRepository staffRepository;
    private final ManufacturersRepository manufacturersRepository;

    public Products addProduct(ProductDto.Create create){
        Products productEntity = create.asEntity(products ->
                products.withManufacturers(manufacturersRepository.findById(create.getProductManufacturerId()).get())
                        .withStaffs(staffRepository.findById(create.getStaffId()).orElse(null)));

        return productsRepository.save(productEntity);
    }

    public void deleteProduct(int productId){
        productsRepository.deleteById(productId);
    }

    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }

    public Products updateProducts(ProductDto.Put put){
        Products findProduct = productsRepository.findByProductId(put.getProductId());

        Products updatedProduct = put.asPutEntity(findProduct);
        return productsRepository.save(updatedProduct);
    }

    public List<Products> findProductsByManufacturersId(int manufacturerId){
        return productsRepository.findByManufacturersManufacturerId(manufacturerId);
    }

}
