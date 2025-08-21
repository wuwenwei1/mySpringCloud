package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.dao.AuditRecordMapper;
import org.demo.security.dao.DataRemittanceReviewMapper;
import org.demo.security.service.AuditRecordServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditRecordServiceExteriorImpl implements AuditRecordServiceExterior {
    @Autowired
    private AuditRecordMapper auditRecordMapper;

    @Transactional
    @Override
    public Result getAuditRecordList(Long id) {
        List<JSONObject> auditRecordList=auditRecordMapper.getAuditRecordList(id);
        return ResultBuilder.aResult().code("2000").data(auditRecordList).build();
    }

    /**
     * 删除汇交数据的审核意见
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result delReviewsIdea(Long id) {
        auditRecordMapper.delReviewsIdeaById(id);
        return ResultBuilder.aResult().msg("删除成功!").code("2000").build();
    }
}
