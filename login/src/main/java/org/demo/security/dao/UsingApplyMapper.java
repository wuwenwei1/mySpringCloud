package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.UsingApply;

public interface UsingApplyMapper {

    void addUsingApply(@Param("usingApply") UsingApply usingApply);

    UsingApply getUsingApplyById(@Param("id")Long id);

    JSONObject getUsingApplyReviewDataById(@Param("id")Long id);

    void usingApplyReview(@Param("usingApply")UsingApply usingApply);
}