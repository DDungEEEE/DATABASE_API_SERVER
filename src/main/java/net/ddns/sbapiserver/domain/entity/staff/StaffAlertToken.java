package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "staff_alert_token")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class StaffAlertToken {

    @Id @Column(name = "staff_alert_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffAlertTokenId;

    @Column(name = "alert_token")
    private String alertToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "token_number")
    private String tokenNumber;

    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @OneToOne
    private Staffs staffs;

}
