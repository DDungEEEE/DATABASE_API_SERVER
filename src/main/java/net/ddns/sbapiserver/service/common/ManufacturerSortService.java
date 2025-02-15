package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerSortDto;
import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.repository.common.ManufacturerSortRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManufacturerSortService {
    private final ManufacturerSortRepository manufacturerSortRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final ProductsRepository productsRepository;

    @Transactional
    public List<ManufacturerSortDto.Result> getAllManufacturerSort(){
        List<ManufacturerSort> allManufacturerSorts = manufacturerSortRepository.findAll();
        return ManufacturerSortDto.Result.of(allManufacturerSorts);
    }

    @Transactional
    public List<ManufacturerSortDto.Result> findManufacturerSortByManufacturerId(int manufacturerId){
        List<ManufacturerSort> all = manufacturerSortRepository.getManufacturerSortByManufacturersManufacturerId(manufacturerId);

        List<ManufacturerSort> sortedManufacturerSorts = all.stream()
                .sorted(Comparator.comparing(ManufacturerSort::getManufacturerSortOrder))
                .collect(Collectors.toList());

        return ManufacturerSortDto.Result.of(sortedManufacturerSorts);
    }

    @Transactional
    public ManufacturerSortDto.Result createManufacturerSort(ManufacturerSortDto.Create create){
        ManufacturerSort createEntity = create.asEntity(sort -> sort.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(create.getManufacturerId())));
        ManufacturerSort saveEntity = manufacturerSortRepository.save(createEntity);
        return ManufacturerSortDto.Result.of(saveEntity);
    }

    @Transactional
    public ManufacturerSortDto.Result updateManufacturerSort(ManufacturerSortDto.Put put){
        ManufacturerSort putEntity = put.asPutEntity(serviceErrorHelper.findManufacturerSortOrElseThrow404(put.getManufacturerSortId()));
        putEntity = putEntity.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(put.getManufacturerId()));
        ManufacturerSort saveEntity = manufacturerSortRepository.save(putEntity);
        return ManufacturerSortDto.Result.of(saveEntity);
    }

    @Transactional
    public void deleteManufacturerSortById(int manufacturerSortId){
        serviceErrorHelper.findManufacturerSortOrElseThrow404(manufacturerSortId);
        productsRepository.setManufacturerSortToNull(manufacturerSortId );
        manufacturerSortRepository.deleteById(manufacturerSortId);
    }


}
