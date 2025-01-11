package net.ddns.sbapiserver.domain.entity.common;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString
@Entity @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "manufacturer_sort")
public class ManufacturerSort {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_sort_id")
    private Integer manufacturerSortId;

    @With
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturers manufacturers;

    @Column(name = "sort_name")
    private String sortName;

    @Column(name = "manufacturer_sort_order")
    private int manufacturerSortOrder;
}
