package net.ddns.sbapiserver.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.alert.ClientAlertTokenDto;
import net.ddns.sbapiserver.service.alert.ClientAlertTokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "고객 알림 컨트롤러")
@RequestMapping("/api/client-alert-token")
@RestController
@RequiredArgsConstructor
public class TestClientAlertTokenController {
    private final ClientAlertTokenService clientAlertTokenService;

    @GetMapping("/{client_id}")
    public ResultResponse<List<ClientAlertTokenDto.Result>> getClientToken(@PathVariable("client_id") int clientId){
        List<ClientAlertTokenDto.Result> findTokens = clientAlertTokenService.findClientAlertTokenByClientId(clientId);
        return ResultResponse.<List<ClientAlertTokenDto.Result>>successResponse()
                .result(findTokens)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "모든 고객 알림토큰 조회")
    @GetMapping
    public ResultResponse<List<ClientAlertTokenDto.Result>> getAllClientTokens(){
        List<ClientAlertTokenDto.Result> allClientAlertTokens = clientAlertTokenService.getAllClientAlertTokens();
        return ResultResponse.<List<ClientAlertTokenDto.Result>>successResponse()
                .result(allClientAlertTokens)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping
    public ResultResponse<ClientAlertTokenDto.Result> createClientToken(@RequestBody ClientAlertTokenDto.Create create){
        ClientAlertTokenDto.Result createToken = clientAlertTokenService.createClientAlertToken(create);
        return ResultResponse.<ClientAlertTokenDto.Result>successResponse()
                .result(createToken)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @DeleteMapping
    public ResultResponse<Void> delete(@RequestBody ClientAlertTokenDto.Delete delete){
        clientAlertTokenService.deleteClientAlertToken(delete);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
