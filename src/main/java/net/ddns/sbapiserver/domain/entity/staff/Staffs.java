package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Entity @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "staffs")
public class Staffs {

    @Id @Column(name = "staff_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    @Column(name = "staff_user_id")
    private String staffUserId;

    @Column(name = "staff_password")
    private String staffPassword;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_position")
    private String staffPosition;

    @Column(name = "staff_department")
    private String staffDepartment;

    @Column(unique = true, name = "staff_phone_number")
    private String staffPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_gender")
    private StaffGender staffGender;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createAt;

    @Column(name = "updated_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updateAt;

}
