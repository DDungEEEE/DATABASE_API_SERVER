package net.ddns.sbapiserver.domain.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Orders {

    @Id @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "order_request", columnDefinition = "TEXT")
    private String orderRequest;

    @Column(name = "order_print_ck")
    private int orderPrintCk;

    @Column(name = "order_status")
    private String orderStatus;

    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @ManyToOne
    private Clients clients;

    @PrePersist
    protected void setOrderDate(){

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.orderDate = now.format(dateTimeFormatter);
    }
}
