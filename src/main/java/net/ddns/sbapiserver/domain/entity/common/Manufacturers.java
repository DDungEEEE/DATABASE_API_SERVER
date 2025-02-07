package net.ddns.sbapiserver.domain.entity.common;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder @Setter
@Table(name = "manufacturers")
public class Manufacturers {

    @Id @Column(name = "manufacturer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer manufacturerId;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Column(name = "manufacturer_img")
    private String manufacturerImg;

    @Column(name = "manufacturer_status", nullable = false, length = 50)
    private String manufacturerStatus;

    @Column(name = "manufacturer_order", nullable = false)
    private int manufacturerOrder;

    @JoinColumn(name = "staff_id")
    @ManyToOne
    private Staffs staffs;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PreUpdate
    private void setUpdatedAt(){
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }


}
