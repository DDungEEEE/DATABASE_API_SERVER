package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.service.authentication.AuthenticationService;
import net.ddns.sbapiserver.service.common.ClientService;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "클라이언트 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
@RestController
public class ClientController {

    private final ClientService clientService;
    private final AuthenticationService authenticationService;
    private final ServiceErrorHelper serviceErrorHelper;

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @Operation(summary = "클라이언트 목록 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResultResponse<List<ClientsDto.Result>> getClientList(){
        List<ClientsDto.Result> clientList = clientService.getClientList();
        return ResultResponse.<List<ClientsDto.Result>>successResponse()
                .result(clientList)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "아이디 중복 검사")
    @GetMapping("/check/{client_name}")
    public ResultResponse<Boolean> checkDuplicatedId(@PathVariable("client_name") String clientName){
        boolean userIdDuplicated = serviceErrorHelper.isUserIdDuplicated(clientName);
        return ResultResponse.<Boolean>successResponse()
                .successCode(SuccessCode.SELECT_SUCCESS)
                .result(userIdDuplicated)
                .build();
    }


    @Operation(summary = "클라이언트 회원 가입")
    @PostMapping
    public ResultResponse<ClientsDto.Result> addClient(@RequestBody @Valid ClientsDto.Create create){
        ClientsDto.Result clientResult = clientService.addClients(create);

        return ResultResponse.<ClientsDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(clientResult)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "클라이언트 수정")
    @PutMapping
    public ResultResponse<ClientsDto.Result> putClient(@RequestBody ClientsDto.Put put, Authentication authentication){
        authenticationService.isOwner(authentication, put.getClientId());

        Clients clients = clientService.updateClients(put);
        ClientsDto.Result resultClients = ClientsDto.Result.of(clients);
        return ResultResponse.<ClientsDto.Result>successResponse()
                .result(resultClients)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @Operation(summary = "클라이언트 삭제")
    @DeleteMapping("{client_id}")
    public ResultResponse<Void> deleteClient(@PathVariable("client_id") int clientId, Authentication authentication){
        authenticationService.isOwner(authentication, clientId);

        clientService.deleteClientsById(clientId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
