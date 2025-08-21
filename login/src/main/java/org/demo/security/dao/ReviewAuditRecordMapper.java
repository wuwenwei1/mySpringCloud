package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.ReviewAuditRecord;

public interface ReviewAuditRecordMapper {

    void addReviewAuditRecord(@Param("reviewAuditRecord") ReviewAuditRecord reviewAuditRecord);

    void updateAuditRecordIdByReviewId(@Param("reviewId")Long reviewId,@Param("auditRecordId") Long auditRecordId);

    JSONObject getDataRemittanceReviewDataByReviewId(@Param("reviewId")Long reviewId);


    ///////////////////////////////////////////////////
}