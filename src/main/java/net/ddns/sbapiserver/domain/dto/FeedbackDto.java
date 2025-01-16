package net.ddns.sbapiserver.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.entity.client.Feedback;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface FeedbackDto {

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "FeedbackCreate")
    class Create{
        @Schema(name = "feedback_content")
        private String feedbackContent;

        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "is_check")
        private int isCheck;

        public Feedback asEntity(Function<? super Feedback, Feedback> init){
            return init.apply(Feedback.builder()
                    .feedbackContent(feedbackContent)
                    .isCheck(isCheck)
                    .build());
        }
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "FeedbackPut")
    class Put{
        @Schema(name = "feedback_id")
        private int feedbackId;
        @Schema(name = "feedback_content")
        private String feedbackContent;
        @Schema(name = "client_id")
        private int clientId;
        @Schema(name = "is_check")
        private int isCheck;

        public Feedback asPutEntity(Feedback feedback){
            feedback.setFeedbackId(feedbackId);
            feedback.setFeedbackContent(feedbackContent);
            feedback.setIsCheck(isCheck);
            return feedback;
        }
    }
    @Builder
    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "FeedbackResult")
    class Result{
        @Schema(name = "feedback_id")
        private int feedbackId;

        @Schema(name = "feedback_content")
        private String feedbackContent;

        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "is_check")
        private int isCheck;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "created_at")
        private Timestamp createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "updated_at")
        private Timestamp updatedAt;

        public static Result of(Feedback feedBack){
            return Result.builder()
                    .feedbackId(feedBack.getFeedbackId())
                    .feedbackContent(feedBack.getFeedbackContent())
                    .clientId(feedBack.getClients().getClientId())
                    .isCheck(feedBack.getIsCheck())
                    .createdAt(feedBack.getCreatedAt())
                    .updatedAt(feedBack.getUpdatedAt())
                    .build();
        }

        public static List<Result> of(List<Feedback> feedBacks){
            return feedBacks.stream().map(Result::of).collect(Collectors.toList());
        }
    }

}
