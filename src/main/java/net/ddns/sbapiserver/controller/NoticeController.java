package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.staff.NoticeDto;
import net.ddns.sbapiserver.service.common.NoticeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "공지사항 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "공지사항 조회")
    @GetMapping
    public ResultResponse<List<NoticeDto.Result>> get(){
        List<NoticeDto.Result> noticeList = noticeService.getNoticeList();
        return ResultResponse.<List<NoticeDto.Result>>successResponse()
                .successCode(SuccessCode.SELECT_SUCCESS)
                .result(noticeList)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "공지사항 검색")
    @GetMapping("/{start_date}/{end_date}")
    public ResultResponse<List<NoticeDto.Result>> search(@PathVariable("start_date") @DateTimeFormat(pattern = "yy-MM-dd") LocalDate startDate,
                                                         @PathVariable("end_date") @DateTimeFormat(pattern = "yy-MM-dd") LocalDate endDate){
        List<NoticeDto.Result> searchNotice = noticeService.searchNotice(startDate, endDate);
        return ResultResponse.<List<NoticeDto.Result>>successResponse()
                .result(searchNotice)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PreAuthorize(value = "'ROLE_STAFF'")
    @Operation(summary = "공지사항 등록")
    @PostMapping
    public ResultResponse<NoticeDto.Result> add(@RequestBody NoticeDto.Create create){
        NoticeDto.Result notice = noticeService.createNotice(create);

        return ResultResponse.<NoticeDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(notice)
                .build();
    }

    @PreAuthorize(value = "'ROLE_STAFF'")
    @Operation(summary = "공지사항 수정")
    @PutMapping
    public ResultResponse<NoticeDto.Result> updateNotice(@RequestBody NoticeDto.Put put){
        NoticeDto.Result result = noticeService.updateNotice(put);
        return ResultResponse.<NoticeDto.Result>successResponse()
                .result(result)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @PreAuthorize(value = "'ROLE_STAFF'")
    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("{notice_id}")
    public ResultResponse<Void> deleteNotice(@PathVariable("notice_id") int noticeId){
        noticeService.deleteNotice(noticeId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
