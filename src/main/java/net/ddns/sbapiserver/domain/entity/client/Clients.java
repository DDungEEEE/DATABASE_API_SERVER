package net.ddns.sbapiserver.domain.entity.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clients")
public class Clients {

    @Id @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    @Column(name = "client_name") @NotBlank
    private String clientName;

    @Column(name = "client_password")
    private String clientPassword;

    @Column(name = "client_store_name")
    private String clientStoreName;

    @Column(name = "client_ceo_name")
    private String clientCeoName;

    @Column(name = "client_addr")
    private String clientAddr;

    @Column(name = "client_business_number")
    private String clientBusinessNumber;

    @Column(name = "client_margin_ratio")
    private String clientMarginRatio;

    @Column(name = "client_ph_num")
    private String clientPhNum;

    @Column(name = "client_status")
    private String clientStatus;

    @Column(name = "client_refresh_token")
    private String clientRefreshToken;

    @Column(name = "client_created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp clientCreatedAt;

    @Column(name = "client_updated_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp clientUpdatedAt;

}
