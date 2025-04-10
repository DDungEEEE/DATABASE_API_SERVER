package net.ddns.sbapiserver.domain.dto.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.staff.Notice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public interface NoticeDto {

    @Data
    @NoArgsConstructor
    @Schema(name = "NoticeCreate")
    class Create{

        @Schema(name = "notice_content")
        private String noticeContent;

        @Schema(name = "notice_status")
        private String noticeStatus;

        @Schema(name = "notice_title")
        private String noticeTitle;

        @Schema(name = "staff_id")
        private int staffId;

        public Notice asEntity(Function<? super Notice, Notice> init){
                return init.apply(Notice.builder()
                        .noticeContent(noticeContent)
                        .noticeTitle(noticeTitle)
                        .noticeStatus(noticeStatus)
                        .build());
        }
    }
    @Schema(name = "noticePutDto")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Put{
        @Schema(name = "notice_id")
        private int noticeId;

        @Schema(name = "notice_content")
        private String noticeContent;

        @Schema(name = "notice_status")
        private String noticeStatus;

        @Schema(name = "notice_title")
        private String noticeTitle;

        @Schema(name = "staff_id")
        private int staffId;

        public Notice asPutEntity(Notice notice){
            notice.setNoticeContent(noticeContent);
            notice.setNoticeStatus(noticeStatus);
            notice.setNoticeTitle(noticeTitle);
            return notice;
        }
    }

    @Schema(name = "noticeResult")
    @Data
    @Builder
    class Result{
        @Schema(name = "notice_id")
        private int noticeId;

        @Schema(name = "notice_content")
        private String noticeContent;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime noticeDate;

        @Schema(name = "notice_status")
        private String noticeStatus;

        @Schema(name = "notice_title")
        private String noticeTitle;

        @Schema(name = "staff_id")
        private int staffId;

        @Schema(name = "staff_name")
        private String staffName;

        public static Result of(Notice notice){
            return Result.builder()
                    .noticeContent(notice.getNoticeContent())
                    .noticeId(notice.getNoticeId())
                    .noticeDate(notice.getNoticeDate())
                    .noticeStatus(notice.getNoticeStatus())
                    .staffId(notice.getStaffs().getStaffId())
                    .staffName(notice.getStaffs().getStaffName())
                    .noticeTitle(notice.getNoticeTitle())
                    .build();
        }

        public static List<Result> of(List<Notice> notices){
            return notices.stream().map(Result::of).collect(Collectors.toList());
        }
    }
}
