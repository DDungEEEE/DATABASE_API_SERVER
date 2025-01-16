package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.FeedbackDto;
import net.ddns.sbapiserver.service.common.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "피드백 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Operation(summary = "모든 피드백들 목록 조회")
    @GetMapping
    public ResultResponse<List<FeedbackDto.Result>> getAllFeedbacks(){
        return ResultResponse.<List<FeedbackDto.Result>>
                successResponse()
                .result(feedbackService.getAllFeedbacks())
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "피드백 등록")
    @PostMapping
    public ResultResponse<FeedbackDto.Result> addFeedback(@RequestBody FeedbackDto.Create create){
        FeedbackDto.Result saveFeedback = feedbackService.addFeedback(create);
        return ResultResponse.<FeedbackDto.Result>successResponse()
                .result(saveFeedback)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @Operation(summary = "피드백 수정")
    @PutMapping
    public ResultResponse<FeedbackDto.Result> updateFeedback(@RequestBody FeedbackDto.Put put){
        FeedbackDto.Result updateFeedback = feedbackService.updateFeedback(put);
        return ResultResponse.<FeedbackDto.Result>successResponse()
                .result(updateFeedback)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }
    @Operation(summary = "피드백 삭제")
    @DeleteMapping("/{feedback_id}")
    public ResultResponse<Void> deleteFeedback(@PathVariable(value = "feedback_id") int feedbackId){
        feedbackService.deleteFeedbackById(feedbackId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

}
