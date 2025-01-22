package net.ddns.sbapiserver.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffPasswordEditDto {
    @Schema(name = "staff_id")
    private int staffId;

    @Schema(name = "staff_password")
    private String staffPassword;
}
