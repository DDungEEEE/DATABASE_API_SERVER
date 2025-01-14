//package net.ddns.sbapiserver.sesrvice;
//
//import jakarta.persistence.EntityManager;
//import net.ddns.sbapiserver.domain.dto.common.ProductDto;
//import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
//import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
//import net.ddns.sbapiserver.domain.entity.common.Products;
//import net.ddns.sbapiserver.repository.common.ManufacturerSortRepository;
//import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
//import net.ddns.sbapiserver.repository.common.ProductsRepository;
//import net.ddns.sbapiserver.service.common.ManufacturerService;
//import net.ddns.sbapiserver.service.common.ManufacturerSortService;
//import net.ddns.sbapiserver.service.common.ProductsService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//@Transactional
//@SpringBootTest
//public class ManufacturerSortServiceTest {
//    @Autowired
//    private ManufacturerSortService manufacturerSortService;
//    @Autowired
//    private ManufacturersRepository manufacturersRepository;
//    @Autowired
//    private ManufacturerSortRepository manufacturerSortRepository;
//    @Autowired
//    private ProductsService productsService;
//    @Autowired
//    private ProductsRepository productsRepository;
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    @DisplayName("제품군 삭제 -> 상품들 null로 바뀌는지 확인")
//    public void deleteManufacturerSort(){
//        /**
//         * 테스트 데이터 값들 삽입
//         */
//        Manufacturers testM = Manufacturers.builder()
//                .manufacturerName("dasd")
//                .manufacturerImg("Asdsa")
//                .manufacturerStatus("활성")
//                .build();
//        Manufacturers saveManufacture = manufacturersRepository.save(testM);
//
//        ManufacturerSort testMS = ManufacturerSort.builder()
//                .manufacturers(saveManufacture)
//                .sortName("Asdsa")
//                .build();
//        ManufacturerSort testManufacturerSort = manufacturerSortRepository.save(testMS);
//
//        ProductDto.Create testP1 = ProductDto.Create.builder()
//                .manufacturerSortId(testManufacturerSort.getManufacturerSortId())
//                .productName("asd")
//                .productImg("das")
//                .productStatus("주문 가능")
//                .productManufacturerId(saveManufacture.getManufacturerId())
//                .build();
//
//        ProductDto.Create testP2 = ProductDto.Create.builder()
//                .manufacturerSortId(testManufacturerSort.getManufacturerSortId())
//                .productName("asd")
//                .productImg("das")
//                .productStatus("주문 가능")
//                .productManufacturerId(saveManufacture.getManufacturerId())
//                .build();
//
//        ProductDto.Result result1 = productsService.addProduct(testP1);
//        ProductDto.Result result2 = productsService.addProduct(testP2);
//
//        manufacturerSortService.deleteManufacturerSortById(testManufacturerSort.getManufacturerSortId());
//        entityManager.flush();
//        entityManager.clear();
//        Optional<Products> byId1 = productsRepository.findById(result1.getProductId());
//        Optional<Products> byId2 = productsRepository.findById(result2.getProductId());
//        System.out.println("------------ first data-------" + (byId1.get().getManufacturerSort()));
//        System.out.println("------------ second data--------  " + (byId2.get().getManufacturerSort()));
//
//    }
//}
