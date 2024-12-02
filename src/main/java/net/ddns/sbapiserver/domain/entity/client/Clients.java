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

    @Column(name = "client_ph_num")
    private String clientPhNum;

    @Column(name = "client_status")
    private String clientStatus;

    @Column(name = "client_refresh_token")
    private String clientRefreshToken;

    @CreationTimestamp
    @Column(name = "client_created_at",updatable = false, nullable = false)
    private Timestamp clientCreatedAt;

    @UpdateTimestamp
    @Column(name = "client_updated_at", nullable = false)
    private Timestamp clientUpdatedAt;

    @PrePersist
    private void setClientUpdatedAt(){
        clientUpdatedAt = new Timestamp(System.currentTimeMillis());
    }
}
