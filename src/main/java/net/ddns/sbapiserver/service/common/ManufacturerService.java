package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturersRepository manufacturersRepository;

    public ManufacturerDto.Result CreateManufacturer(ManufacturerDto.Create create){
        Manufacturers createManufacturer = Manufacturers.builder()
                .manufacturerImg(create.getManufacturerImg())
                .manufacturerName(create.getManufacturerName())
                .build();

        Manufacturers saveManufacturer = manufacturersRepository.save(createManufacturer);
        return ManufacturerDto.Result.of(saveManufacturer);
    }

    public List<ManufacturerDto.Result> getManufacturerList(){
        return ManufacturerDto.Result.of(manufacturersRepository.findAll());
    }

    public void deleteManufacturerById(int manufacturerId){
        manufacturersRepository.deleteById(manufacturerId);
    }
}
