package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UsingApplyServiceExterior {
    Result addUsingApply(Long remittanceId,String contactNumber, String email, String contactAddress, String usageScenarios, List<MultipartFile> files);

    Result getUsingApply(Long id);

    Result getUsingApplyReviewData(Long id);

    Result afreshAddUsingApply(Long id,Long remittanceId, String contactNumber, String email, String contactAddress, String usageScenarios, List<MultipartFile> files);

    Result usingApplyReview(Long id, String reviewIdea, Long reviewTypeId);
}
