package org.demo.security.controller;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.DataRemittanceReviewServiceExterior;
import org.demo.security.service.DataRemittanceServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/DataRemittanceReview")
public class DataRemittanceReviewController {

    @Autowired
    private DataRemittanceReviewServiceExterior dataRemittanceReviewServiceExterior;



    /**
     * 汇交数据类型下拉框
     * @return
     */
    @PostMapping("/getRemittanceTypeBox")
    public Result getRemittanceTypeBox(){
        List<Object> remittanceTypeList = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id",0);
        jsonObject1.put("remittanceTypeName","新增");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id",1);
        jsonObject2.put("remittanceTypeName","更新");
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("id",2);
        jsonObject3.put("remittanceTypeName","重提");
        remittanceTypeList.add(jsonObject1);
        remittanceTypeList.add(jsonObject2);
        remittanceTypeList.add(jsonObject3);

        return ResultBuilder.aResult().data(remittanceTypeList).code("2000").build();
    }


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
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/getDataRemittanceReviewList")
    public Result getDataRemittanceReviewList(@RequestParam(required = false) String dataName,
                                              @RequestParam(required = false) Long industryTypeId,
                                              @RequestParam(required = false) Long subjectAreaId,
                                              @RequestParam(required = false) Long dataOpenTypeId,
                                              @RequestParam(required = false) String submitName,
                                              @RequestParam(required = false) Long reviewTypeId,
                                              @RequestParam(required = false) Long remittanceTypeId,
                                              @RequestParam(required = false) String reviewName,
                                              @RequestParam(required = false) String createStartTime,
                                              @RequestParam(required = false) String createEndTime,
                                              @RequestParam Integer pageSize,
                                              @RequestParam Integer pageNum){
        if(null==dataName|| StringUtils.isEmpty(dataName.replace(" ",""))){
            dataName="";
        }else {
            dataName=dataName.replace(" ","");
        }
        if(null==submitName|| StringUtils.isEmpty(submitName.replace(" ",""))){
            submitName="";
        }else {
            submitName=submitName.replace(" ","");
        }
        if(null==reviewName|| StringUtils.isEmpty(reviewName.replace(" ",""))){
            reviewName="";
        }else {
            reviewName=reviewName.replace(" ","");
        }
        if(null==createStartTime|| StringUtils.isEmpty(createStartTime.replace(" ",""))){
            createStartTime="";
        }
        if(null==createEndTime|| StringUtils.isEmpty(createEndTime.replace(" ",""))){
            createEndTime="";
        }
        Integer starIndex = (pageNum - 1) * pageSize;
        return dataRemittanceReviewServiceExterior.getDataRemittanceReviewList(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,submitName,reviewTypeId,remittanceTypeId,reviewName,createStartTime,createEndTime,starIndex,pageSize);
    }

    /**
     * 获取汇交数据的历史审核列表
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceReviewHis")
    public Result getDataRemittanceReviewHis(@RequestParam Long id,
                                             @RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize){

        int newStarSize = (pageNum - 1) * pageSize;

        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceReviewServiceExterior.getDataRemittanceReviewHis(id,newStarSize, pageSize);
    }

    /**
     * 根据副本数据id,查看单个副本数据详情
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceReview")
    public Result getDataRemittanceReview(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceReviewServiceExterior.getDataRemittanceReview(id);
    }

    /**
     * 根据汇交数据Id,查看最新副本数据详情
     * @param remittanceId
     * @return
     */
    @PostMapping("/getDataRemittanceReviewByRemittanceId")
    public Result getDataRemittanceReviewByRemittanceId(@RequestParam Long remittanceId){
        if(null==remittanceId){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceReviewServiceExterior.getDataRemittanceReviewByRemittanceId(remittanceId);
    }

    /**
     * 数据审核
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @PostMapping("/dataReview")
    public Result dataReview(@RequestParam Long id,
                             @RequestParam(required = false) String reviewIdea,
                             @RequestParam Long reviewTypeId){



        if(null==id){
            return ResultBuilder.aResult().msg("请选择审核的数据!").code("7000").build();
        }
        if(null==reviewIdea|| StringUtils.isEmpty(reviewIdea.replace(" ",""))){
            reviewIdea="";
        }

        if(null==reviewTypeId){
            return ResultBuilder.aResult().msg("请选择审核类型!").code("7000").build();
        }else {
            if(2!=reviewTypeId&&3!=reviewTypeId){
                return ResultBuilder.aResult().msg("审核类型错误!").code("7000").build();
            }
        }
        return dataRemittanceReviewServiceExterior.dataReview(id,reviewIdea, reviewTypeId);
    }

    /**
     * 数据反审
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @PostMapping("/dataReverseReview")
    public Result dataReverseReview(@RequestParam Long id,
                                    @RequestParam(required = false) String reviewIdea,
                                    @RequestParam Long reviewTypeId){



        if(null==id){
            return ResultBuilder.aResult().msg("请选择审核的数据!").code("7000").build();
        }
        if(null==reviewIdea|| StringUtils.isEmpty(reviewIdea.replace(" ",""))){
            reviewIdea="";
        }

        if(null==reviewTypeId){
            return ResultBuilder.aResult().msg("请选择审核类型!").code("7000").build();
        }else {
            if(2!=reviewTypeId&&3!=reviewTypeId){
                return ResultBuilder.aResult().msg("审核类型错误!").code("7000").build();
            }
        }
        return dataRemittanceReviewServiceExterior.dataReverseReview(id,reviewIdea, reviewTypeId);
    }

    /**
     * 查看汇交数据副本的审核信息
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceReviewData")
    public Result getDataRemittanceReviewData(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceReviewServiceExterior.getDataRemittanceReviewData(id);
    }


    ///////////////////////////////////////////

}
