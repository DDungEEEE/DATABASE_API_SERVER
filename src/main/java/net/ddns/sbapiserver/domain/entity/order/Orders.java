package net.ddns.sbapiserver.domain.entity.order;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
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
    private Integer orderId;

    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_request", columnDefinition = "TEXT")
    private String orderRequest;

    @Column(name = "order_print_ck", nullable = false)
    private int orderPrintCk;

    @Column(name = "order_status")
    private String orderStatus;

    @With
    @JoinColumn(name = "client_id")
    @ManyToOne
    private Clients clients;

    public void setOrderPrintCk(int orderPrintCk) {
        this.orderPrintCk = orderPrintCk;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @PrePersist
    protected void checkOrderEntity(){
        this.orderDate = LocalDateTime.now();
        this.orderPrintCk = 0;
        this.orderStatus = "관리자 승인 대기중";
    }
}
