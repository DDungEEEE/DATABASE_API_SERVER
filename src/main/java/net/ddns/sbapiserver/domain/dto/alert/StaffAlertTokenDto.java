package net.ddns.sbapiserver.domain.dto.alert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.staff.StaffAlertToken;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface StaffAlertTokenDto {

    @Data @Schema(name = "CreateStaffAlertToken")
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
                            .alertToken(alertToken)
                            .deviceInfo(deviceInfo)
                            .tokenNumber(tokenNumber)
                            .build());
        }
    }
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "DeleteStaffAlertToken")
    @Data
    class Delete{
        @Schema(name = "token_number")
        private int tokenNumber;
        @Schema(name = "staff_id")
        private int staffId;
    }

    @Schema(name = "ResultStaffAlertToken")
    @Data
    @Builder
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

        public static Result of(StaffAlertToken staffAlertToken){
            return Result.builder()
                    .staffAlertTokenId(staffAlertToken.getStaffAlertTokenId())
                    .alertToken(staffAlertToken.getAlertToken())
                    .deviceInfo(staffAlertToken.getDeviceInfo())
                    .tokenNumber(staffAlertToken.getTokenNumber())
                    .staffId(staffAlertToken.getStaffs().getStaffId())
                    .build();
        }

        public static List<Result> of(List<StaffAlertToken> staffAlertTokens){
            return staffAlertTokens.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
