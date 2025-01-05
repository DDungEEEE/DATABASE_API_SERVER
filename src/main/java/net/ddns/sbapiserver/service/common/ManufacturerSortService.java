package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerSortDto;
import net.ddns.sbapiserver.domain.entity.common.ManufacturerSort;
import net.ddns.sbapiserver.repository.common.ManufacturerSortRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerSortService {
    private final ManufacturerSortRepository manufacturerSortRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    public List<ManufacturerSortDto.Result> getAllManufacturerSort(){
        List<ManufacturerSort> allManufacturerSorts = manufacturerSortRepository.findAll();
        return allManufacturerSorts == null ? null : ManufacturerSortDto.Result.of(allManufacturerSorts);
    }

    public List<ManufacturerSortDto.Result> findManufacturerSortByManufacturerId(int manufacturerId){
        return ManufacturerSortDto.Result.of(manufacturerSortRepository.getManufacturerSortByManufacturersManufacturerId(manufacturerId));
    }

    public ManufacturerSortDto.Result createManufacturerSort(ManufacturerSortDto.Create create){
        ManufacturerSort createEntity = create.asEntity(sort -> sort.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(create.getManufacturerId())));
        ManufacturerSort saveEntity = manufacturerSortRepository.save(createEntity);
        return ManufacturerSortDto.Result.of(saveEntity);
    }

    public ManufacturerSortDto.Result updateManufacturerSort(ManufacturerSortDto.Put put){
        ManufacturerSort putEntity = put.asPutEntity(serviceErrorHelper.findManufacturerSortOrElseThrow404(put.getManufacturerSortId()));
        putEntity = putEntity.withManufacturers(serviceErrorHelper.findManufacturerOrElseThrow404(put.getManufacturerId()));
        ManufacturerSort saveEntity = manufacturerSortRepository.save(putEntity);
        return ManufacturerSortDto.Result.of(saveEntity);
    }

    public void deleteManufacturerSortById(int manufacturerSortId){
        serviceErrorHelper.findManufacturerSortOrElseThrow404(manufacturerSortId);
        manufacturerSortRepository.deleteById(manufacturerSortId);
    }

}
