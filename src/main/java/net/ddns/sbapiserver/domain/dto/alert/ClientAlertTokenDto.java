package net.ddns.sbapiserver.domain.dto.alert;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.ClientAlertToken;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ClientAlertTokenDto {

    @Data @Schema(name = "CreateClientAlertToken")
    @AllArgsConstructor
    @NoArgsConstructor
    class Create{
        @Schema(name = "alert_token")
        private String alertToken;
        @Schema(name = "device_info")
        private String deviceInfo;
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "client_id")
        private int clientId;

        public ClientAlertToken asEntity(Function<? super ClientAlertToken, ClientAlertToken> init){
            return init.apply(
                    ClientAlertToken
                            .builder()
                            .alertToken(alertToken)
                            .deviceInfo(deviceInfo)
                            .tokenNumber(tokenNumber)
                            .build());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "DeleteClientAlertToken")
    @Data
    class Delete{
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "client_id")
        private int clientId;
    }

    @Schema(name = "ResultClientAlertToken")
    @Data
    @Builder
    class Result{
        @Schema(name = "client_alert_token_id")
        private int clientAlertTokenId;
        @Schema(name = "alert_token")
        private String alertToken;
        @Schema(name = "device_info")
        private String deviceInfo;
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "client_id")
        private int clientId;

        public static Result of(ClientAlertToken clientAlertToken){
            return Result.builder()
                    .clientAlertTokenId(clientAlertToken.getClientAlertTokenId())
                    .alertToken(clientAlertToken.getAlertToken())
                    .deviceInfo(clientAlertToken.getDeviceInfo())
                    .tokenNumber(clientAlertToken.getTokenNumber())
                    .clientId(clientAlertToken.getClients().getClientId())
                    .build();
        }

        public static List<Result> of(List<ClientAlertToken> clientAlertTokens){
            return clientAlertTokens.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
