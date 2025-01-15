package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.FeedbackDto;
import net.ddns.sbapiserver.domain.entity.client.Feedback;
import net.ddns.sbapiserver.repository.common.FeedbackRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional
    public FeedbackDto.Result addFeedback(FeedbackDto.Create create){
        Feedback createFeedback = create.asEntity(feedBack ->
                feedBack.withClients(serviceErrorHelper.findClientsOrElseThrow404(create.getClientId())));
        Feedback saveEntity = feedbackRepository.save(createFeedback);
        return FeedbackDto.Result.of(saveEntity);
    }
    @Transactional
    public FeedbackDto.Result updateFeedback(FeedbackDto.Put put){
        Feedback findFeedback = serviceErrorHelper.findFeedbackOrElseThrow404(put.getFeedbackId());
        Feedback putEntity = put.asPutEntity(findFeedback).withClients(serviceErrorHelper.findClientsOrElseThrow404(put.getClientId()));
        return FeedbackDto.Result.of(feedbackRepository.save(putEntity));
    }

    @Transactional(readOnly = true)
    public List<FeedbackDto.Result> getAllFeedbacks(){
        return FeedbackDto.Result.of(feedbackRepository.findAll());
    }

    @Transactional
    public void deleteFeedbackById(int feedbackId){
        feedbackRepository.deleteById(feedbackId);
    }

}
