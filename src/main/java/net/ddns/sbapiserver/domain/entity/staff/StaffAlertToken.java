package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "staff_alert_token")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Getter @Builder
public class StaffAlertToken {

    @Id @Column(name = "staff_alert_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffAlertTokenId;

    @Column(name = "alert_token")
    private String alertToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "token_number")
    private int tokenNumber;

    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @OneToOne
    private Staffs staffs;

}
