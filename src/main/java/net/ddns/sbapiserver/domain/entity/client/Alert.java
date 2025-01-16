package net.ddns.sbapiserver.domain.entity.client;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Getter @Setter @Builder
@Table(name = "alert")
public class Alert {
    @Id
    @Column(name = "alert_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alertId;

    @Lob
    @Column(name = "alert_content")
    private String alertContent;

    @Column(name = "alert_create_date")
    private String alertCreateDate;

    @With
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients clients;
}
