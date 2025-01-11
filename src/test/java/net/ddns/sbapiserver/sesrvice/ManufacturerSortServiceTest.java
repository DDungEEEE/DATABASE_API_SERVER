package net.ddns.sbapiserver.sesrvice;

import net.ddns.sbapiserver.domain.dto.common.ProductDto;
import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.repository.common.ManufacturerSortRepository;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.service.common.ManufacturerService;
import net.ddns.sbapiserver.service.common.ManufacturerSortService;
import net.ddns.sbapiserver.service.common.ProductsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class ManufacturerSortServiceTest {
    @Autowired
    private ManufacturerSortService manufacturerSortService;
    @Autowired
    private ManufacturersRepository manufacturersRepository;
    @Autowired
    private ManufacturerSortRepository manufacturerSortRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ProductsRepository productsRepository;

    @Test
    @DisplayName("제품군 삭제 -> 상품들 null로 바뀌는지 확인")
    public void deleteManufacturerSort(){
        /**
         * 테스트 데이터 값들 삽입
         */
        Manufacturers testM = Manufacturers.builder()
                .manufacturerName("dasd")
                .manufacturerImg("Asdsa")
                .manufacturerStatus("활성")
                .build();
        Manufacturers saveManufacture = manufacturersRepository.save(testM);

        ManufacturerSort testMS = ManufacturerSort.builder()
                .manufacturers(saveManufacture)
                .sortName("Asdsa")
                .build();
        ManufacturerSort testManufacturerSort = manufacturerSortRepository.save(testMS);

        ProductDto.Create testP1 = ProductDto.Create.builder()
                .manufacturerSortId(testManufacturerSort.getManufacturerSortId())
                .productName("asd")
                .productImg("das")
                .productStatus("주문 가능")
                .productManufacturerId(saveManufacture.getManufacturerId())
                .build();

        ProductDto.Create testP2 = ProductDto.Create.builder()
                .manufacturerSortId(testManufacturerSort.getManufacturerSortId())
                .productName("asd")
                .productImg("das")
                .productStatus("주문 가능")
                .productManufacturerId(saveManufacture.getManufacturerId())
                .build();
        ProductDto.Create testP3 = ProductDto.Create.builder()
                .manufacturerSortId(testManufacturerSort.getManufacturerSortId())
                .productName("asd")
                .productImg("das")
                .productStatus("주문 가능")
                .productManufacturerId(saveManufacture.getManufacturerId())
                .build();

        ProductDto.Create testP4 = ProductDto.Create.builder()
                .manufacturerSortId(9)
                .productName("asd")
                .productImg("das")
                .productStatus("주문 가능")
                .productManufacturerId(saveManufacture.getManufacturerId())
                .build();
        productsService.addProduct(testP1);
        productsService.addProduct(testP2);
        productsService.addProduct(testP3);
        productsService.addProduct(testP4);
        manufacturerSortService.deleteManufacturerSortById(testManufacturerSort.getManufacturerSortId());
        List<Products> byManufacturersManufacturerId = productsRepository.getProductsByManufacturerSortManufacturerSortId(testManufacturerSort.getManufacturerSortId());
        System.out.println(Arrays.toString(byManufacturersManufacturerId.toArray()));

    }
}
