package net.ddns.sbapiserver.domain.entity.client;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "client_alert_token")
public class ClientAlertToken {

    @Id @Column(name = "client_alert_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientAlertTokenId;

    @Column(name = "alert_token")
    private String alertToken;

    @Column(name = "devicer_info")
    private String deviceInfo;

    @Column(name = "token_number")
    private int tokenNumber;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Clients clients;
}
