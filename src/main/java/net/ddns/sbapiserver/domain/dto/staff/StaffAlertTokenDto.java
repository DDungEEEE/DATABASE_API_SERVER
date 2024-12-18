package net.ddns.sbapiserver.domain.dto.staff;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.staff.StaffAlertToken;

import java.util.function.Function;

public interface StaffAlertTokenDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Create{
        @Schema(name = "alert_token")
        private String alertToken;
        @Schema(name = "device_info")
        private String deviceInfo;
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "staff_id")
        private int staffId;

        public StaffAlertToken asEntity(Function<? super StaffAlertToken, StaffAlertToken> init){
            return init.apply(
                    StaffAlertToken.builder()
                            .deviceInfo(deviceInfo)
                            .tokenNumber(tokenNumber)
                            .build()
            );
        }
    }
    @Data
    class Delete{
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "staff_id")
        private int staffId;
    }

    @Data
    class Result{
        @Schema(name = "staff_alert_token_id")
        private int staffAlertTokenId;
        @Schema(name = "alert_token")
        private String alertToken;
        @Schema(name = "device_info")
        private String deviceInfo;
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "staff_id")
        private int staffId;
    }
}
