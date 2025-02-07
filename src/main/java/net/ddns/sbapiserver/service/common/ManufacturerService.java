package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.common.ManufacturerSortRepository;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturersRepository manufacturersRepository;
    private final ProductsRepository productsRepository;
    private final ManufacturerSortRepository manufacturerSortRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional
    public ManufacturerDto.Result CreateManufacturer(ManufacturerDto.Create create){
        Integer manufacturerOrder = create.getManufacturerOrder() == null ? 1 : create.getManufacturerOrder();
        Staffs findStaffs = serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId());

        Manufacturers createManufacturer = Manufacturers.builder()
                .manufacturerImg(create.getManufacturerImg())
                .manufacturerName(create.getManufacturerName())
                .manufacturerStatus(create.getManufacturerStatus())
                .manufacturerOrder(manufacturerOrder)
                .staffs(findStaffs)
                .build();

        Manufacturers saveManufacturer = manufacturersRepository.save(createManufacturer);
        saveManufacturer.setManufacturerOrder(saveManufacturer.getManufacturerId());

        Manufacturers orderSaveManufacturer = manufacturersRepository.save(saveManufacturer);
        return ManufacturerDto.Result.of(orderSaveManufacturer);
    }

    @Transactional
    public ManufacturerDto.Result updateManufacturer(ManufacturerDto.Put put){
        Manufacturers putManufacturer = put.asPutEntity();
        Staffs findStaffs = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        putManufacturer.setStaffs(findStaffs);
        Manufacturers saveManufacturer = manufacturersRepository.save(putManufacturer);
        return ManufacturerDto.Result.of(saveManufacturer);
    }

    @Transactional(readOnly = true)
    public List<ManufacturerDto.Result> getManufacturerList(){
        List<Manufacturers> allManufacturers = manufacturersRepository.findAll();
        List<Manufacturers> sortedManufacturers = allManufacturers.stream().sorted(Comparator.comparing(manufacturers -> manufacturers.getManufacturerOrder())).collect(Collectors.toList());
        return ManufacturerDto.Result.of(sortedManufacturers);
    }

    @Transactional
    public void deleteManufacturerById(int manufacturerId){
        productsRepository.deleteAllByManufacturersManufacturerId(manufacturerId);
        manufacturerSortRepository.deleteAllByManufacturersManufacturerId(manufacturerId);
        manufacturersRepository.deleteById(manufacturerId);
    }
}
