package net.ddns.sbapiserver.sesrvice;


import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.service.common.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ClientServiceTest{
    @Autowired
    private ClientService clientService;

    @Test
    @DisplayName("클라이언트 회원가입 테스트 전 기본 Client 등록")
    public void ClientSignUp(){
        ClientsDto.Create newClients = ClientsDto.Create
                .builder()
                .clientName("asd123")
                .clientPassword("asdasd")
                .clientStoreName("경주 십원빵")
                .clientCeoName("손원익")
                .clientAddr("경주시 노동동")
                .clientBusinessNumber("12341234")
                .clientMarginRatio("0")
                .clientPhNum("123456")
                .clientStatus("활성")
                .build();

        ClientsDto.Result result = clientService.addClients(newClients);
        clientService.addClients(newClients);
        System.out.println(result.toString());
    }
}
