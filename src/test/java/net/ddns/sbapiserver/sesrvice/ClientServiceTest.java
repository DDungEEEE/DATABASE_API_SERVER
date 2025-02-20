package net.ddns.sbapiserver.sesrvice;


import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.service.common.ClientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class ClientServiceTest{
    @Autowired
    private ClientService clientService;

//    @BeforeEach
//    public void signUpDefaultClient(){
//        ClientsDto.Create newClients = ClientsDto.Create
//                .builder()
//                .clientName("asd123")
//                .clientPassword("asdasd")
//                .clientStoreName("경주 십원빵")
//                .clientCeoName("손원익")
//                .clientAddr("경주시 노동동")
//                .clientBusinessNumber("12341234")
//                .clientMarginRatio("0")
//                .clientPhNum("123456")
//                .clientStatus("활성")
//                .build();
//
//        clientService.addClients(newClients);
//    }
//
//    @Test
//    @DisplayName("클라이언트 회원가입시 사용자 아이디 중복 검사")
//    public void DuplicateClientsNameSignUp(){
//        ClientsDto.Create duplicateNameClient = ClientsDto.Create
//                .builder()
//                .clientName("asd123")
//                .clientPassword("asdasd")
//                .clientStoreName("경주 십원빵")
//                .clientCeoName("손원익")
//                .clientAddr("경주시 노동동")
//                .clientBusinessNumber("1234122234")
//                .clientMarginRatio("0")
//                .clientPhNum("12345612321")
//                .clientStatus("활성")
//                .build();
//        clientService.addClients(duplicateNameClient);
//
//    }
//
//    @Test
//    @DisplayName("클라이언트 회원가입시 사용자 전화번호 중복 검사")
//    public void DuplicateClientPhoneNumberSignUp(){
//        ClientsDto.Create duplicatePhoneNumClient = ClientsDto.Create
//                .builder()
//                .clientName("asd12asdsa3")
//                .clientPassword("asdasd")
//                .clientStoreName("경주 십원빵")
//                .clientCeoName("손원익")
//                .clientAddr("경주시 노동동")
//                .clientBusinessNumber("12341234")
//                .clientMarginRatio("0")
//                .clientPhNum("123456")
//                .clientStatus("활성")
//                .build();
//
//        clientService.addClients(duplicatePhoneNumClient);
//    }

//    @Test
//    @DisplayName("주소로 위도 경도 추출 테스트")
//    public void allUserGeoCodingTest(){
//        List<ClientsDto.Result> allClient = clientService.getClientList();
//
//        List<String> clientAddr = allClient.stream().map(ClientsDto.Result::getClientAddr).toList();
//        for(String addr : clientAddr){
//            Map<String, String> locationByClientAd = clientService.getLocationByClientAd(addr);
//            System.out.println(locationByClientAd.toString());
//        }
//    }
}
