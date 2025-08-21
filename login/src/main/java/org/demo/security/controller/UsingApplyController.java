package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.DataRemittanceServiceExterior;
import org.demo.security.service.UsingApplyServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/UsingApply")
public class UsingApplyController {

    @Autowired
    private UsingApplyServiceExterior usingApplyServiceExterior;

    /**
     * 对使用数据申请进行审核
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @PostMapping("/usingApplyReview")
    public Result usingApplyReview(@RequestParam Long id,
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
        return usingApplyServiceExterior.usingApplyReview(id,reviewIdea, reviewTypeId);
    }

    /**
     * 查看数据使用申请的审核数据
     * @param id
     * @return
     */
    @PostMapping("/getUsingApplyReviewData")
    public Result getUsingApplyReviewData(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return usingApplyServiceExterior.getUsingApplyReviewData(id);
    }

    /**
     * 查看数据申请详情的回显
     * @param id
     * @return
     */
    @PostMapping("/getUsingApply")
    public Result getUsingApply(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return usingApplyServiceExterior.getUsingApply(id);
    }

    /**
     * 重新提交数据使用申请
     * @param remittanceId
     * @param contactNumber
     * @param email
     * @param contactAddress
     * @param usageScenarios
     * @param files
     * @return
     */
    @PostMapping("/afreshAddUsingApply")
    public Result afreshAddUsingApply(@RequestParam Long id,
                                      @RequestParam Long remittanceId,
                                      @RequestParam(required = false) String contactNumber,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(required = false) String contactAddress,
                                      @RequestParam(required = false) String usageScenarios,
                                      @RequestParam(required = false)List<MultipartFile> files){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }
        if(null==remittanceId){
            return ResultBuilder.aResult().msg("请选择申请的数据!").code("7000").build();
        }
        if(null==contactNumber|| StringUtils.isEmpty(contactNumber.replace(" ",""))){
            contactNumber="";
            return ResultBuilder.aResult().msg("请填写联系电话!").code("7000").build();
        }else {
            contactNumber=contactNumber.replace(" ","");
        }
        if(null==email|| StringUtils.isEmpty(email.replace(" ",""))){
            email="";
            /*return ResultBuilder.aResult().msg("请填写联系邮箱!").code("7000").build();*/
        }else {
            email=email.replace(" ","");
        }
        if(null==contactAddress|| StringUtils.isEmpty(contactAddress.replace(" ",""))){
            contactAddress="";
            /*return ResultBuilder.aResult().msg("请填写联系地址!").code("7000").build();*/
        }else {
            contactAddress=contactAddress.replace(" ","");
        }
        if(null==usageScenarios|| StringUtils.isEmpty(usageScenarios.replace(" ",""))){
            contactAddress="";
            /*return ResultBuilder.aResult().msg("请填写使用场景!").code("7000").build();*/
        }else {
            usageScenarios=usageScenarios.replace(" ","");
        }
        return usingApplyServiceExterior.afreshAddUsingApply(id,remittanceId,contactNumber,email,contactAddress,usageScenarios,files);
    }


    /**
     * 添加数据使用申请
     * @param contactNumber
     * @param email
     * @param contactAddress
     * @param usageScenarios
     * @return
     */
    @PostMapping("/addUsingApply")
    public Result addUsingApply(@RequestParam Long remittanceId,
                                @RequestParam(required = false) String contactNumber,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String contactAddress,
                                @RequestParam(required = false) String usageScenarios,
                                @RequestParam(required = false)List<MultipartFile> files){
        if(null==remittanceId){
            return ResultBuilder.aResult().msg("请选择申请的数据!").code("7000").build();
        }
        if(null==contactNumber|| StringUtils.isEmpty(contactNumber.replace(" ",""))){
            contactNumber="";
            return ResultBuilder.aResult().msg("请填写联系电话!").code("7000").build();
        }else {
            contactNumber=contactNumber.replace(" ","");
        }
        if(null==email|| StringUtils.isEmpty(email.replace(" ",""))){
            email="";
            /*return ResultBuilder.aResult().msg("请填写联系邮箱!").code("7000").build();*/
        }else {
            email=email.replace(" ","");
        }
        if(null==contactAddress|| StringUtils.isEmpty(contactAddress.replace(" ",""))){
            contactAddress="";
            /*return ResultBuilder.aResult().msg("请填写联系地址!").code("7000").build();*/
        }else {
            contactAddress=contactAddress.replace(" ","");
        }
        if(null==usageScenarios|| StringUtils.isEmpty(usageScenarios.replace(" ",""))){
            contactAddress="";
            /*return ResultBuilder.aResult().msg("请填写使用场景!").code("7000").build();*/
        }else {
            usageScenarios=usageScenarios.replace(" ","");
        }
        return usingApplyServiceExterior.addUsingApply(remittanceId,contactNumber,email,contactAddress,usageScenarios,files);
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
   /* @PostMapping("/afreshAddDataRemittance")
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
*/



}
