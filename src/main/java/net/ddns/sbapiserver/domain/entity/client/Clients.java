package net.ddns.sbapiserver.domain.entity.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity @Setter
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clients")
public class Clients {

    @Id @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;

    @Column(name = "client_name")
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

    @Column(name = "client_ph_num", unique = true)
    private String clientPhNum;

    @Column(name = "client_status")
    private String clientStatus;

    @Column(name = "client_refresh_token")
    private String clientRefreshToken;

    @Column(name = "client_do")
    private String clientDo;

    @Column(name = "client_si")
    private String clientSi;

    @Column(name = "client_created_at",updatable = false, nullable = false)
    private Timestamp clientCreatedAt;

    @Column(name = "client_updated_at", nullable = false)
    private Timestamp clientUpdatedAt;

    @Column(name = "client_lag")
    private String clientLag;

    @Column(name = "client_long")
    private String clientLong;

    @Column(name = "client_login_time")
    private Timestamp clientLoginTime;

    @PrePersist
    private void setClintTime(){
        this.clientCreatedAt = new Timestamp(System.currentTimeMillis());
        this.clientUpdatedAt = new Timestamp(System.currentTimeMillis());
    }
    @PreUpdate
    private void setClientUpdatedAt(){
        this.clientUpdatedAt = new Timestamp(System.currentTimeMillis());
    }
}
