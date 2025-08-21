package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.AuditRecord;

import java.util.List;

public interface AuditRecordMapper {

    void addAuditRecord(@Param("auditRecord")AuditRecord auditRecord);
    ///////////////////////////

    List<JSONObject> getAuditRecordList(@Param("id") Long id);



    void delReviewsIdeaById(@Param("id")Long id);
}