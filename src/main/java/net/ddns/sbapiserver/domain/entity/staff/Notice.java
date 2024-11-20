package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity @Getter
@Builder @Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice {

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    @Column(name = "notice_content", columnDefinition = "TEXT")
    private String noticeContent;


    @Column(name = "notice_date")
    private LocalDateTime noticeDate;

    @Column(name = "notice_status")
    private String noticeStatus;

    @Column(name = "notice_title")
    private String noticeTitle;

    @With
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staffs staffs;

    @PrePersist
    protected void setNoticeDate(){
        noticeDate = LocalDateTime.now().withSecond(0).withNano(0);
    }
}
