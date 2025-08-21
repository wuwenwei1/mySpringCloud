package org.demo.security.controller;

import org.checkerframework.checker.units.qual.A;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.IndustryTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/IndustryType")
public class IndustryTypeController {
    @Autowired
    private IndustryTypeServiceExteror industryTypeServiceExteror;


    /**
     * 行业名称下拉框
     * @return
     */
    @PostMapping("/getIndustryTypeBox")
    public Result getIndustryTypeBox(){
        return industryTypeServiceExteror.getIndustryTypeBox();
    }



    /**
     * 行业名称管理列表
     * @param industryName
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/getIndustryTypeList")
    public Result getIndustryTypeList(@RequestParam(required = false) String industryName,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam Integer pageSize,
                                      @RequestParam Integer pageNum){
        if(null==industryName|| StringUtils.isEmpty(industryName.replace(" ",""))){
            industryName="";
        }else {
            industryName=industryName.replace(" ","");
        }

        if(null!=status){
            if(0!=status&&1!=status){
                return ResultBuilder.aResult().msg("状态参数值异常!").code("7000").build();
            }
        }
        Integer starIndex = (pageNum - 1) * pageSize;
        return industryTypeServiceExteror.getIndustryTypeList(industryName,status,starIndex,pageSize);
    }



    /**
     * 修改行业名称状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/updateIndustryTypeStatus")
    public Result updateIndustryTypeStatus(@RequestParam Long id,@RequestParam Integer status){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }

        if(null==status){
            return ResultBuilder.aResult().msg("请选择状态!").code("7000").build();
        }else {
            if(0!=status&&1!=status){
                return ResultBuilder.aResult().msg("状态参数值异常!").code("7000").build();
            }
        }
        return industryTypeServiceExteror.updateIndustryTypeStatus(id,status);
    }



    /**
     * 删除行业名称
     * @param id
     * @return
     */
    @PostMapping("/delIndustryType")
    public Result delIndustryType(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }
        return industryTypeServiceExteror.delIndustryType(id);
    }

    /**
     * 添加行业名称
     * @param industryName
     * @return
     */
    @PostMapping("/addIndustryType")
    public Result addIndustryType(@RequestParam String industryName){
        if(null==industryName|| StringUtils.isEmpty(industryName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写行业名称!").code("7000").build();
        }else {
            industryName=industryName.replace(" ","");
        }

        return industryTypeServiceExteror.addIndustryType(industryName);
    }


    /**
     * 修改行业名称
     * @param industryName
     * @return
     */
    @PostMapping("/updateIndustryType")
    public Result updateIndustryType(@RequestParam Long id,@RequestParam String industryName){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }
        if(null==industryName|| StringUtils.isEmpty(industryName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写行业名称!").code("7000").build();
        }else {
            industryName=industryName.replace(" ","");
        }

        return industryTypeServiceExteror.updateIndustryType(id,industryName);
    }
}
