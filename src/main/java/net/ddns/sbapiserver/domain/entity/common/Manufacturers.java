package net.ddns.sbapiserver.domain.entity.common;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder
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


    private int manufacturerOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PreUpdate
    private void setUpdatedAt(){
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
