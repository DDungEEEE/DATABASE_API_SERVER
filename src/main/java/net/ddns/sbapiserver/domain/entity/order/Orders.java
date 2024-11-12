package net.ddns.sbapiserver.domain.entity.order;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Orders {

    @Id @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(name = "order_date", nullable = false, updatable = false)
    private String orderDate;

    @Column(name = "order_request", columnDefinition = "TEXT")
    private String orderRequest;

    @Column(name = "order_print_ck", nullable = false)
    private int orderPrintCk;

    @Column(name = "order_status")
    private String orderStatus;

    @With
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @ManyToOne
    private Clients clients;

    @PrePersist
    protected void checkOrderEntity(){

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.orderDate = now.format(dateTimeFormatter);

        this.orderPrintCk = 0;
        this.orderStatus = "관리자 승인 대기중";
    }
}
