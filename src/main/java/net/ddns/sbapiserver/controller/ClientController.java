package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.service.common.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/client")
@RestController
public class ClientController {

    private final ClientService clientService;

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
    @Operation(summary = "클라이언트 회원 가입")
    @PostMapping
    public ResultResponse<ClientsDto.Result> addClient(@RequestBody ClientsDto.Create create){
        ClientsDto.Result clientResult = clientService.addClients(create);

        return ResultResponse.<ClientsDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(clientResult)
                .build();
    }

    @Operation(summary = "클라이언트 수정")
    @PutMapping
    public ResultResponse<ClientsDto.Result> putClient(@RequestBody ClientsDto.Put put){
        Clients clients = clientService.updateClients(put);
        ClientsDto.Result resultClients = ClientsDto.Result.of(clients);
        return ResultResponse.<ClientsDto.Result>successResponse()
                .result(resultClients)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @Operation(summary = "클라이언트 수정")
    @DeleteMapping("{client_id}")
    public ResultResponse<Void> deleteClient(@PathVariable("client_id") int clientId){
        clientService.deleteClientsById(clientId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
