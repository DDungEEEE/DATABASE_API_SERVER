package net.ddns.sbapiserver.domain.entity.staff;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice {

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeId;

    @Column(name = "notice_content", columnDefinition = "TEXT")
    private String noticeContent;

    @Column(name = "notice_date")
    private String noticeDate;

    @Column(name = "notice_status")
    private String noticeStatus;

    @Column(name = "notice_title")
    private String notice_title;

    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staffs staffs;

    @PrePersist
    protected void setNoticeDate(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");

        this.noticeDate = now.format(dateTimeFormatter);
    }
}
