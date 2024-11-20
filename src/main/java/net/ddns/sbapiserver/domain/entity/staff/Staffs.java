package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@Entity @Getter
@Builder @Setter
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

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Timestamp updateAt;

    @PrePersist
    protected void setStaffTime(){
        createAt = new Timestamp(System.currentTimeMillis());
        updateAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void updateStaffTime(){
        updateAt = new Timestamp(System.currentTimeMillis());
    }
}
