package net.ddns.sbapiserver.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.notice.NoticeDto;
import net.ddns.sbapiserver.service.common.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notice")
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResultResponse<List<NoticeDto.Result>> get(){
        List<NoticeDto.Result> noticeList = noticeService.getNoticeList();
        return ResultResponse.<List<NoticeDto.Result>>successResponse()
                .successCode(SuccessCode.SELECT_SUCCESS)
                .result(noticeList)
                .build();
    }

    @PostMapping
    public ResultResponse<NoticeDto.Result> add(@RequestBody NoticeDto.Create create){
        NoticeDto.Result notice = noticeService.createNotice(create);

        return ResultResponse.<NoticeDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(notice)
                .build();
    }
}
