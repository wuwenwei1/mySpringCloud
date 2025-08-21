package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.DataRemittanceReview;

import java.util.List;

public interface DataRemittanceReviewMapper {
    void addDataRemittanceReview(@Param("dataRemittanceReview") DataRemittanceReview dataRemittanceReview);
    DataRemittanceReview getDataRemittanceReviewId(@Param("id")Long id);

    void updateReviewTypeIdById(@Param("id")Long id,@Param("reviewTypeId")Long reviewTypeId);

    JSONObject getDataRemittanceReviewById(@Param("id")Long id);
    ///////////////////////////////////////////



    List<JSONObject> getDataRemittanceReviewHisByRemittanceId(@Param("remittanceId")Long remittanceId,@Param("newStarSize")Integer newStarSize,@Param("pageSize")Integer pageSize);

    Long getDataRemittanceReviewHisTotalByRemittanceId(@Param("remittanceId")Long remittanceId);



}