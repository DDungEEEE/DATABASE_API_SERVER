package net.ddns.sbapiserver.service.alert;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.AlertDto;
import net.ddns.sbapiserver.domain.entity.client.Alert;
import net.ddns.sbapiserver.repository.common.AlertRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional(readOnly = true)
    public List<AlertDto.Result> getAllAlertList(){
        List<Alert> alertList = alertRepository.findAll();
        return AlertDto.Result.of(alertList);
    }

    @Transactional
    public AlertDto.Result createAlert(AlertDto.Create create){
        Alert createAlert = create.asEntity(alert -> alert.withClients(serviceErrorHelper.findClientsOrElseThrow404(create.getClientId())));
        Alert saveAlert = alertRepository.save(createAlert);
        return AlertDto.Result.of(saveAlert);
    }

    @Transactional
    public AlertDto.Result updateAlert(AlertDto.Put put){
        Alert findAlert = serviceErrorHelper.findAlertOrElseThrow404(put.getAlertId());
        Alert updateAlert = put.asPutEntity(findAlert).withClients(serviceErrorHelper.findClientsOrElseThrow404(put.getClientId()));
        return AlertDto.Result.of(alertRepository.save(updateAlert));
    }

    @Transactional
    public void deleteAlert(int alertId){
        alertRepository.deleteById(alertId);
    }
}
