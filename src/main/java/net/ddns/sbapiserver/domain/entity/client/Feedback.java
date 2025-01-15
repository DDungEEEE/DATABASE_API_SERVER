package net.ddns.sbapiserver.domain.entity.client;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Getter @Builder @Setter
@Table(name = "feedback")
public class Feedback {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @Column(name = "feedback_content")
    private String feedbackContent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @With
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients clients;

    @Column(name = "is_check")
    private int isCheck;

    @PrePersist
    private void setFeedbackTime(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    @PreUpdate
    private void updateFeedbackTime(){
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
