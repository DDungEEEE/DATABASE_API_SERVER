package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturersRepository manufacturersRepository;

    @Transactional
    public ManufacturerDto.Result CreateManufacturer(ManufacturerDto.Create create){
        Manufacturers createManufacturer = Manufacturers.builder()
                .manufacturerImg(create.getManufacturerImg())
                .manufacturerName(create.getManufacturerName())
                .manufacturerStatus(create.getManufacturerStatus())
                .build();

        Manufacturers saveManufacturer = manufacturersRepository.save(createManufacturer);
        return ManufacturerDto.Result.of(saveManufacturer);
    }

    @Transactional
    public ManufacturerDto.Result updateManufacturer(ManufacturerDto.Put put){
        Manufacturers putManufacturer = put.asPutEntity();
        Manufacturers saveManufacturer = manufacturersRepository.save(putManufacturer);
        return ManufacturerDto.Result.of(saveManufacturer);
    }

    @Transactional(readOnly = true)
    public List<ManufacturerDto.Result> getManufacturerList(){
        return ManufacturerDto.Result.of(manufacturersRepository.findAll());
    }

    @Transactional
    public void deleteManufacturerById(int manufacturerId){
        manufacturersRepository.deleteById(manufacturerId);
    }
}
