package net.ddns.sbapiserver.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.FeedbackDto;
import net.ddns.sbapiserver.service.common.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vi/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping
    public ResultResponse<List<FeedbackDto.Result>> getAllFeedbacks(){
        return ResultResponse.<List<FeedbackDto.Result>>
                successResponse()
                .result(feedbackService.getAllFeedbacks())
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping
    public ResultResponse<FeedbackDto.Result> addFeedback(@RequestBody FeedbackDto.Create create){
        FeedbackDto.Result saveFeedback = feedbackService.addFeedback(create);
        return ResultResponse.<FeedbackDto.Result>successResponse()
                .result(saveFeedback)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @PutMapping
    public ResultResponse<FeedbackDto.Result> updateFeedback(@RequestBody FeedbackDto.Put put){
        FeedbackDto.Result updateFeedback = feedbackService.updateFeedback(put);
        return ResultResponse.<FeedbackDto.Result>successResponse()
                .result(updateFeedback)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }
    @DeleteMapping("/{feedback_id}")
    public ResultResponse<Void> deleteFeedback(@PathVariable(value = "feedback_id") int feedbackId){
        feedbackService.deleteFeedbackById(feedbackId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

}
