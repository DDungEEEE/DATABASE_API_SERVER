package net.ddns.sbapiserver.domain.dto.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.staff.StaffGender;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.function.Function;


public interface StaffDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name = "StaffCreate")
    class Create{

        @NotBlank(message = "아이디는 공백일 수 없습니다.")
        @Schema(name = "staff_user_id")
        private String staffUserId;

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Schema(name = "staff_password")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{1,16}$", message = "15자 이하의 영 대문자, 특수문자를 조합해주세요")
        private String staffPassword;

        @Schema(name = "staff_name")
        private String staffName;

        @Schema(name = "staff_position")
        private String staffPosition;

        @Schema(name = "staff_department")
        private String staffDepartment;

        @Schema(name = "staff_phone_number")
        private String staffPhoneNumber;

        @Schema(name = "staff_gender")
        private StaffGender staffGender;

        public Staffs asEntity(){
            return Staffs.builder()
                    .staffUserId(staffUserId)
                    .staffDepartment(staffDepartment)
                    .staffName(staffName)
                    .staffPassword(staffPassword)
                    .staffGender(staffGender)
                    .staffPhoneNumber(staffPhoneNumber)
                    .staffPosition(staffPosition)
                    .build();
        }
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @Schema(name = "StaffUpdate")
    class Put{
        @Schema(name = "staff_id")
        private int staffId;


        @Schema(name = "staff_name")
        private String staffName;

        @Schema(name = "staff_position")
        private String staffPosition;

        @Schema(name = "staff_department")
        private String staffDepartment;

        @Schema(name = "staff_phone_number")
        private String staffPhoneNumber;

        @Schema(name = "staff_gender")
        private StaffGender staffGender;

        public Staffs asPutEntity(Staffs staffs){
            staffs.setStaffId(staffId);
            staffs.setStaffName(staffName);
            staffs.setStaffPosition(staffPosition);
            staffs.setStaffDepartment(staffDepartment);
            staffs.setStaffPhoneNumber(staffPhoneNumber);
            staffs.setStaffGender(staffGender);
            return staffs;
        }
    }
    @Data
    @Builder
    @Schema(name = "StaffResult")
    class Result{

        @Schema(name = "staff_id")
        private int staffId;

        @Schema(name = "staff_user_id")
        private String staffUserId;

        @Schema(name = "staff_password")
        private String staffPassword;

        @Schema(name = "staff_name")
        private String StaffName;

        @Schema(name = "staff_position")
        private String staffPosition;

        @Schema(name = "staff_department")
        private String staffDepartment;

        @Schema(name = "staff_phone_number")
        private String staffPhoneNumber;

        @Schema(name = "staff_gender")
        private StaffGender staffGender;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "created_at")
        private Timestamp createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "updated_at")
        private Timestamp updatedAt;

        @Schema(name = "staff_refresh_token")
        private String staffRefreshToken;

        public static Result of(Staffs staffs){
            return Result.builder()
                    .staffUserId(staffs.getStaffUserId())
                    .staffId(staffs.getStaffId())
                    .staffPassword(staffs.getStaffPassword())
                    .StaffName(staffs.getStaffName())
                    .staffGender(staffs.getStaffGender())
                    .staffPhoneNumber(staffs.getStaffPhoneNumber())
                    .staffDepartment(staffs.getStaffDepartment())
                    .staffPosition(staffs.getStaffPosition())
                    .createdAt(staffs.getCreateAt())
                    .updatedAt(staffs.getUpdateAt())
                    .staffRefreshToken(staffs.getStaffRefreshToken())
                    .build();
        }
    }
}
