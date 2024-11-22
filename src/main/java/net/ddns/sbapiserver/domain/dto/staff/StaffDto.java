package net.ddns.sbapiserver.domain.dto.staff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.staff.StaffGender;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;

import java.sql.Timestamp;
import java.util.function.Function;

@Schema(name = "StaffDto")
public interface StaffDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Create{

        @Schema(name = "staff_user_id")
        private String staffUserId;

        @Schema(name = "staff_password")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z!@#$%^&*(),.?\":{}|<>]{1,16}$", message = "15자 이하의 영 대문자, 특수문자를 조합해주세요")
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
                    .staffUserId(this.staffUserId)
                    .staffDepartment(this.staffDepartment)
                    .staffName(this.staffName)
                    .staffPassword(this.staffPassword)
                    .staffGender(this.staffGender)
                    .staffPhoneNumber(this.staffPhoneNumber)
                    .staffPosition(this.staffPosition)
                    .build();
        }
    }

    @Data
    @Builder
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

        @Schema(name = "created_at")
        private Timestamp createdAt;

        @Schema(name = "updated_at")
        private Timestamp updatedAt;

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
                    .build();
        }
    }
}
