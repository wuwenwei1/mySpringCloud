package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.dao.DataRemittanceMapper;
import org.demo.security.service.DataRemittanceServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/DataRemittance")
public class DataRemittanceController {

    @Autowired
    private DataRemittanceServiceExterior dataRemittanceServiceExterior;


    /**
     * 汇交数据管理列表
     * @param dataName
     * @param industryTypeId
     * @param subjectAreaId
     * @param dataOpenTypeId
     * @param reviewTypeId
     * @param createStartTime
     * @param createEndTime
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/getDataRemittanceList")
    public Result getDataRemittanceList(@RequestParam(required = false) String dataName,
                                      @RequestParam(required = false) Long industryTypeId,
                                      @RequestParam(required = false) Long subjectAreaId,
                                      @RequestParam(required = false) Long dataOpenTypeId,
                                      @RequestParam(required = false) Long reviewTypeId,
                                      @RequestParam(required = false) String createStartTime,
                                      @RequestParam(required = false) String createEndTime,
                                      @RequestParam Integer pageSize,
                                      @RequestParam Integer pageNum){
        if(null==dataName|| StringUtils.isEmpty(dataName.replace(" ",""))){
            dataName="";
        }else {
            dataName=dataName.replace(" ","");
        }

        if(null==createStartTime|| StringUtils.isEmpty(createStartTime.replace(" ",""))){
            createStartTime="";
        }

        if(null==createEndTime|| StringUtils.isEmpty(createEndTime.replace(" ",""))){
            createEndTime="";
        }

        Integer starIndex = (pageNum - 1) * pageSize;
        return dataRemittanceServiceExterior.getDataRemittanceList(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,reviewTypeId,createStartTime,createEndTime,starIndex,pageSize);
    }







    /**
     * 查看/更新时单个数据汇交回显
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittance")
    public Result getDataRemittance(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceServiceExterior.getDataRemittance(id);
    }


    /**
     * 重新提交数据汇交
     * @param dataName
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @PostMapping("/afreshAddDataRemittance")
    public Result afreshAddDataRemittance(@RequestParam Long id,
                                          @RequestParam String dataName,
                                          @RequestParam Long dataOpenTypeId,
                                          @RequestParam Long subjectAreaId,
                                          @RequestParam Long industryTypeId,
                                          @RequestParam String dataType,
                                          @RequestParam String dataFormat,
                                          @RequestParam(required = false)String remark,
                                          @RequestParam(required = false)List<MultipartFile> files){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择汇交数据!").code("7000").build();
        }
        if(null==dataName|| StringUtils.isEmpty(dataName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据名称!").code("7000").build();
        }else {
            dataName=dataName.replace(" ","");
        }

        if(null==dataOpenTypeId){
            return ResultBuilder.aResult().msg("请选择开放类型!").code("7000").build();
        }
        if(null==subjectAreaId){
            return ResultBuilder.aResult().msg("请选择主题领域!").code("7000").build();
        }
        if(null==industryTypeId){
            return ResultBuilder.aResult().msg("请选择行业分类!").code("7000").build();
        }

        if(null==dataType|| StringUtils.isEmpty(dataType.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据类型!").code("7000").build();
        }else {
            dataType=dataType.replace(" ","");
        }

        if(null==dataFormat|| StringUtils.isEmpty(dataFormat.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据格式!").code("7000").build();
        }else {
            dataFormat=dataFormat.replace(" ","");
        }

        if(null==remark|| StringUtils.isEmpty(remark.replace(" ",""))){
            remark="";
        }else {
            remark=remark.replace(" ","");
        }
        return dataRemittanceServiceExterior.afreshAddDataRemittance(id,dataName,dataOpenTypeId,subjectAreaId,industryTypeId,dataType,dataFormat,remark,files);
    }

    /**
     * 查看汇交数据的审核信息
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceReviewData")
    public Result getDataRemittanceReviewData(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceServiceExterior.getDataRemittanceReviewData(id);
    }

    /**
     * 更新数据汇交
     * @param dataName
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @PostMapping("/updateDataRemittance")
    public Result updateDataRemittance(@RequestParam Long id,
                                       @RequestParam String dataName,
                                       @RequestParam Long dataOpenTypeId,
                                       @RequestParam Long subjectAreaId,
                                       @RequestParam Long industryTypeId,
                                       @RequestParam String dataType,
                                       @RequestParam String dataFormat,
                                       @RequestParam(required = false)String remark,
                                       @RequestParam(required = false)List<MultipartFile> files){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择汇交数据!").code("7000").build();
        }
        if(null==dataName|| StringUtils.isEmpty(dataName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据名称!").code("7000").build();
        }else {
            dataName=dataName.replace(" ","");
        }

        if(null==dataOpenTypeId){
            return ResultBuilder.aResult().msg("请选择开放类型!").code("7000").build();
        }
        if(null==subjectAreaId){
            return ResultBuilder.aResult().msg("请选择主题领域!").code("7000").build();
        }
        if(null==industryTypeId){
            return ResultBuilder.aResult().msg("请选择行业分类!").code("7000").build();
        }

        if(null==dataType|| StringUtils.isEmpty(dataType.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据类型!").code("7000").build();
        }else {
            dataType=dataType.replace(" ","");
        }

        if(null==dataFormat|| StringUtils.isEmpty(dataFormat.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据格式!").code("7000").build();
        }else {
            dataFormat=dataFormat.replace(" ","");
        }

        if(null==remark|| StringUtils.isEmpty(remark.replace(" ",""))){
            remark="";
        }else {
            remark=remark.replace(" ","");
        }
        return dataRemittanceServiceExterior.updateDataRemittance(id,dataName,dataOpenTypeId,subjectAreaId,industryTypeId,dataType,dataFormat,remark,files);
    }

    /**
     * 添加数据汇交
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @PostMapping("/addDataRemittance")
    public Result addDataRemittance(@RequestParam String dataName,
                                    @RequestParam Long dataOpenTypeId,
                                    @RequestParam Long subjectAreaId,
                                    @RequestParam Long industryTypeId,
                                    @RequestParam String dataType,
                                    @RequestParam String dataFormat,
                                    @RequestParam(required = false)String remark,
                                    @RequestParam(required = false)List<MultipartFile> files){
        if(null==dataName|| StringUtils.isEmpty(dataName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据名称!").code("7000").build();
        }else {
            dataName=dataName.replace(" ","");
        }

        if(null==dataOpenTypeId){
            return ResultBuilder.aResult().msg("请选择开放类型!").code("7000").build();
        }
        if(null==subjectAreaId){
            return ResultBuilder.aResult().msg("请选择主题领域!").code("7000").build();
        }
        if(null==industryTypeId){
            return ResultBuilder.aResult().msg("请选择行业分类!").code("7000").build();
        }

        if(null==dataType|| StringUtils.isEmpty(dataType.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据类型!").code("7000").build();
        }else {
            dataType=dataType.replace(" ","");
        }

        if(null==dataFormat|| StringUtils.isEmpty(dataFormat.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写数据格式!").code("7000").build();
        }else {
            dataFormat=dataFormat.replace(" ","");
        }

        if(null==remark|| StringUtils.isEmpty(remark.replace(" ",""))){
            remark="";
        }else {
            remark=remark.replace(" ","");
        }
        return dataRemittanceServiceExterior.addDataRemittance(dataName,dataOpenTypeId,subjectAreaId,industryTypeId,dataType,dataFormat,remark,files);
    }
    ///////////////////////////////////////////////

}
