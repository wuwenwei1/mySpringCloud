package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.bind.annotation.RequestParam;

public interface DataRemittanceReviewServiceExterior {
    Result getDataRemittanceReviewHis(Long id,Integer newStarSize,Integer pageSize);

    Result dataReview(Long id, String reviewIdea, Long reviewTypeId);

    Result dataReverseReview(Long id, String reviewIdea, Long reviewTypeId);

    Result getDataRemittanceReview(Long id);


    Result getDataRemittanceReviewData(Long id);

    Result getDataRemittanceReviewByRemittanceId(Long remittanceId);

    Result getDataRemittanceReviewList(String dataName, Long industryTypeId, Long subjectAreaId, Long dataOpenTypeId, String submitName, Long reviewTypeId, Long remittanceType, String reviewName, String createStartTime, String createEndTime, Integer starIndex, Integer pageSize);
}
