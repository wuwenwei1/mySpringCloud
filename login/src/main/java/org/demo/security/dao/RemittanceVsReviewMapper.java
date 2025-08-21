package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.RemittanceVsReview;

public interface RemittanceVsReviewMapper {

    JSONObject getDataRemittanceReviewDataByRemittanceId(@Param("remittanceId")Long remittanceId);

    void updateRemittanceVsReviewByRemittanceId(@Param("remittanceId")Long remittanceId,@Param("reviewId") Long reviewId);

    void addRemittanceVsReview(@Param("remittanceVsReview") RemittanceVsReview remittanceVsReview);

    JSONObject getDataRemittanceReviewByRemittanceId(@Param("remittanceId")Long remittanceId);
    //////////////////////////////////////






}