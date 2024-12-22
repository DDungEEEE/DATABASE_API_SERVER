package net.ddns.sbapiserver.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


public interface ClientsDto {

    @Schema(name = "ClientCreate")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    class Create{
        @NotBlank(message = "아이디는 공백일 수 없습니다.")
        @Schema(name = "client_name")
        private String clientName;

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Schema(name = "client_password")
        private String clientPassword;

        @Schema(name = "client_store_name")
        private String clientStoreName;

        @NotBlank(message = "고객 이름은 공백일 수 없습니다.")
        @Schema(name = "client_ceo_name")
        private String clientCeoName;

        @Schema(name = "client_addr")
        private String clientAddr;

        @Schema(name = "client_business_number")
        private String clientBusinessNumber;

        @Schema(name = "client_margin_ratio")
        private String clientMarginRatio;

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호는 010-XXXX-XXXX 형식이어야 합니다.")
        @Schema(name = "client_ph_num")
        private String clientPhNum;

        @Schema(name = "client_status")
        private String clientStatus;

        public Clients asEntity(){
            return Clients.builder()
                    .clientName(clientName)
                    .clientPassword(clientPassword)
                    .clientStoreName(clientStoreName)
                    .clientCeoName(clientCeoName)
                    .clientAddr(clientAddr)
                    .clientBusinessNumber(clientBusinessNumber)
                    .clientMarginRatio(clientMarginRatio)
                    .clientPhNum(clientPhNum)
                    .clientStatus(clientStatus)
                    .build();
        }
    }

    @Schema(name = "ClientPut")
    @Data
    @NoArgsConstructor
    class Put{
        @Schema(name = "client_id")
        private int clientId;

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Schema(name = "client_password")
        private String clientPassword;

        @Schema(name = "client_store_name")
        private String clientStoreName;

        @NotBlank(message = "고객 이름은 공백일 수 없습니다.")
        @Schema(name = "client_ceo_name")
        private String clientCeoName;

        @Schema(name = "client_addr")
        private String clientAddr;

        @Schema(name = "client_business_number")
        private String clientBusinessNumber;

        @Schema(name = "client_margin_ratio")
        private String clientMarginRatio;

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호는 010-XXXX-XXXX 형식이어야 합니다.")
        @Schema(name = "client_ph_num")
        private String clientPhNum;

        @Schema(name = "client_status")
        private String clientStatus;

        public Clients asPutEntity(Clients clients){

            clients.setClientStoreName(clientStoreName);
            clients.setClientCeoName(clientCeoName);
            clients.setClientAddr(clientAddr);
            clients.setClientBusinessNumber(clientBusinessNumber);
            clients.setClientMarginRatio(clientMarginRatio);
            clients.setClientPhNum(clientPhNum);
            clients.setClientStatus(clientStatus);

            return clients;
        }
    }

    @Schema(name = "clientResult")
    @Data
    @Builder
    class Result{
        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "client_name")
        private String clientName;

        @Schema(name = "client_password")
        private String clientPassword;

        @Schema(name = "client_store_name")
        private String clientStoreName;

        @Schema(name = "client_ceo_name")
        private String clientCeoName;

        @Schema(name = "client_addr")
        private String clientAddr;

        @Schema(name = "client_business_number")
        private String clientBusinessNumber;

        @Schema(name = "client_margin_ratio")
        private String clientMarginRatio;

        @Schema(name = "client_ph_num")
        private String clientPhNum;

        @Schema(name = "client_status")
        private String clientStatus;

        @Schema(name = "client_refresh_token")
        private String clientRefreshToken;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "client_created_at")
        private Timestamp clientCreatedAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "client_updated_at")
        private Timestamp clientUpdatedAt;

        public static Result of(Clients clients){
            return Result.builder()
                    .clientId(clients.getClientId())
                    .clientName(clients.getClientName())
                    .clientPassword(clients.getClientPassword())
                    .clientCeoName(clients.getClientCeoName())
                    .clientAddr(clients.getClientAddr())
                    .clientBusinessNumber(clients.getClientBusinessNumber())
                    .clientMarginRatio(clients.getClientMarginRatio())
                    .clientPhNum(clients.getClientPhNum())
                    .clientStatus(clients.getClientStatus())
                    .clientRefreshToken(clients.getClientRefreshToken())
                    .clientCreatedAt(clients.getClientCreatedAt())
                    .clientUpdatedAt(clients.getClientUpdatedAt())
                    .build();
        }

        public static List<Result> of(List<Clients> clients){
            return clients.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
