package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.*;
import org.demo.security.entity.AuditRecord;
import org.demo.security.entity.DataRemittance;
import org.demo.security.entity.DataRemittanceReview;
import org.demo.security.entity.UsingApplyFile;
import org.demo.security.service.DataRemittanceReviewServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DataRemittanceReviewServiceExteriorImpl implements DataRemittanceReviewServiceExterior {
    @Autowired
    private DataRemittanceReviewMapper dataRemittanceReviewMapper;
    @Autowired
    private DataRemittanceMapper dataRemittanceMapper;
    @Autowired
    private AuditRecordMapper auditRecordMapper;
    @Autowired
    private ReviewAuditRecordMapper reviewAuditRecordMapper;

    @Autowired
    private RemittanceVsReviewMapper remittanceVsReviewMapper;




    /**
     * 汇交数据审核管理列表
     * @param dataName
     * @param industryTypeId
     * @param subjectAreaId
     * @param dataOpenTypeId
     * @param submitName
     * @param reviewTypeId
     * @param remittanceTypeId
     * @param reviewName
     * @param createStartTime
     * @param createEndTime
     * @param starIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReviewList(String dataName, Long industryTypeId, Long subjectAreaId, Long dataOpenTypeId, String submitName, Long reviewTypeId, Long remittanceTypeId, String reviewName, String createStartTime, String createEndTime, Integer starIndex, Integer pageSize) {
        List<JSONObject> dataRemittanceReviewList=dataRemittanceReviewMapper.getDataRemittanceReviewList(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,submitName,reviewTypeId,remittanceTypeId,reviewName,createStartTime,createEndTime,starIndex,pageSize);
        Long total=dataRemittanceReviewMapper.getDataRemittanceReviewTotal(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,submitName,reviewTypeId,remittanceTypeId,reviewName,createStartTime,createEndTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",dataRemittanceReviewList);
        jsonObject.put("total",total);
        return ResultBuilder.aResult().data(jsonObject).code("2000").build();
    }



    /**
     * 查看汇交数据副本的审核信息
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReviewData(Long id) {
        JSONObject reviewData=reviewAuditRecordMapper.getDataRemittanceReviewDataByReviewId(id);
        return ResultBuilder.aResult().code("2000").data(reviewData).build();
    }

    /**
     * 根据汇交数据Id,查看最新副本数据详情
     * @param remittanceId
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReviewByRemittanceId(Long remittanceId) {
        JSONObject dataRemittanceReview=remittanceVsReviewMapper.getDataRemittanceReviewByRemittanceId(remittanceId);
        return ResultBuilder.aResult().code("2000").data(dataRemittanceReview).build();
    }




    /**
     * 数据审核
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @Transactional
    @Override
    public Result dataReview(Long id, String reviewIdea, Long reviewTypeId) {
        DataRemittanceReview dataRemittanceReview=dataRemittanceReviewMapper.getDataRemittanceReviewId(id);
        if(null!=dataRemittanceReview){
            DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceByIdAndLockIn(dataRemittanceReview.getRemittanceId());

            if(1L!=dataRemittanceReview.getReviewTypeId()){
                return ResultBuilder.aResult().msg("数据已审核过,请不要重复审核!").code("7000").build();
            }
            Long userId = SecurityUtils.getUserId();
            dataRemittanceMapper.updateReviewTypeIdById(oldDataRemittance.getId(),reviewTypeId);
            dataRemittanceReviewMapper.updateReviewTypeIdById(id,reviewTypeId);

            AuditRecord auditRecord = new AuditRecord();
            auditRecord.setReviewId(id);
            auditRecord.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
            auditRecord.setReviewBy(userId);
            auditRecord.setReviewIdea(reviewIdea);
            auditRecordMapper.addAuditRecord(auditRecord);

            reviewAuditRecordMapper.updateAuditRecordIdByReviewId(dataRemittanceReview.getId(),auditRecord.getId());

            return ResultBuilder.aResult().msg("审核成功").code("2000").build();
        }else {
            return ResultBuilder.aResult().msg("数据不存在").code("7000").build();
        }
    }
    ///////////////////////////
    /**
     * 根据副本数据id,查看单个副本数据详情
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReview(Long id) {
        JSONObject dataRemittanceReview=dataRemittanceReviewMapper.getDataRemittanceReviewById(id);
        return ResultBuilder.aResult().code("2000").data(dataRemittanceReview).build();
    }


    /**
     * 数据反审
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @Transactional
    @Override
    public Result dataReverseReview(Long id, String reviewIdea, Long reviewTypeId) {
        DataRemittanceReview dataRemittanceReview=dataRemittanceReviewMapper.getDataRemittanceReviewId(id);
        if(null!=dataRemittanceReview){
            DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceByIdAndLockIn(dataRemittanceReview.getRemittanceId());

            if(1L==dataRemittanceReview.getReviewTypeId()){
                return ResultBuilder.aResult().msg("数据还未审核，不允许反审!").code("7000").build();
            }else {
                if(reviewTypeId==dataRemittanceReview.getReviewTypeId()){
                    String a="";
                    if(2L==reviewTypeId){
                        a="数据审核已是未通过，请不要重复操作!";
                    } else if (3L==reviewTypeId) {
                        a="数据审核已是通过，请不要重复操作!";
                    }
                    return ResultBuilder.aResult().msg(a).code("7000").build();
                }
            }
            Long userId = SecurityUtils.getUserId();
            dataRemittanceMapper.updateReviewTypeIdById(oldDataRemittance.getId(),reviewTypeId);
            dataRemittanceReviewMapper.updateReviewTypeIdById(id,reviewTypeId);
            AuditRecord auditRecord = new AuditRecord();
            auditRecord.setReviewId(id);
            auditRecord.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
            auditRecord.setReviewBy(userId);
            auditRecord.setReviewIdea(reviewIdea);
            auditRecordMapper.addAuditRecord(auditRecord);
            reviewAuditRecordMapper.updateAuditRecordIdByReviewId(dataRemittanceReview.getId(),auditRecord.getId());
            return ResultBuilder.aResult().msg("反审成功").code("2000").build();
        }else {
            return ResultBuilder.aResult().msg("数据不存在").code("7000").build();
        }
    }



    /**
     * 获取汇交数据的历史审核列表
     * @param id
     * @return
     */
    @Override
    public Result getDataRemittanceReviewHis(Long id,Integer newStarSize,Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        Long total = 0L;
        List<JSONObject> dataRemittanceReviewHis=dataRemittanceReviewMapper.getDataRemittanceReviewHisByRemittanceId(id,newStarSize, pageSize);
        total=dataRemittanceReviewMapper.getDataRemittanceReviewHisTotalByRemittanceId(id);
        jsonObject.put("total",total);
        jsonObject.put("data",dataRemittanceReviewHis);
        return ResultBuilder.aResult().code("2000").data(jsonObject).build();
    }


}
