package net.ddns.sbapiserver.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Alert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface AlertDto {
    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "alertCreate")
    class Create{
        @Schema(name = "alert_content")
        private String alertContent;

        @Schema(name = "alert_create_date")
        private String alertCreateDate;

        @Schema(name = "client_id")
        private int clientId;

        public Alert asEntity(Function<? super Alert, ? extends Alert> init){
            return init.apply(
                    Alert.builder()
                    .alertContent(alertContent)
                    .alertCreateDate(alertCreateDate)
                    .build());
        }
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "alertPut")
    class Put{
        @Schema(name = "alert_id")
        private int alertId;

        @Schema(name = "alert_content")
        private String alertContent;

        @Schema(name = "alert_create_date")
        private String alertCreateDate;

        @Schema(name = "client_id")
        private int clientId;

        public Alert asPutEntity(Alert alert){
            alert.setAlertId(alertId);
            alert.setAlertContent(alertContent);
            alert.setAlertContent(alertContent);
            alert.setAlertCreateDate(alertCreateDate);
            return alert;
        }
    }
    @Data
    @Builder
    @Schema(name = "alertResult")
    class Result{
        @Schema(name = "alert_id")
        private int alertId;

        @Schema(name = "alert_content")
        private String alertContent;

        @Schema(name = "alert_create_date")
        private String alertCreateDate;

        @Schema(name = "client_id")
        private int clientId;

        public static Result of(Alert alert){
            return Result.builder()
                    .alertId(alert.getAlertId())
                    .alertContent(alert.getAlertContent())
                    .alertCreateDate(alert.getAlertCreateDate())
                    .clientId(alert.getClients().getClientId())
                    .build();
        }

        public static List<Result> of(List<Alert> alerts){
            return alerts.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
