package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.SubjectAreaServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SubjectArea")
public class SubjectAreaController {
    @Autowired
    private SubjectAreaServiceExteror subjectAreaServiceExteror;


    /**
     * 主题领域下拉框
     * @return
     */
    @PostMapping("/getSubjectAreaBox")
    public Result getSubjectAreaBox(){
        return subjectAreaServiceExteror.getSubjectAreaBox();
    }



    /**
     * 主题领域管理列表
     * @param subjectName
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/getSubjectAreaList")
    public Result getSubjectAreaList(@RequestParam(required = false) String subjectName,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam Integer pageSize,
                                      @RequestParam Integer pageNum){
        if(null==subjectName|| StringUtils.isEmpty(subjectName.replace(" ",""))){
            subjectName="";
        }else {
            subjectName=subjectName.replace(" ","");
        }

        if(null!=status){
            if(0!=status&&1!=status){
                return ResultBuilder.aResult().msg("状态参数值异常!").code("7000").build();
            }
        }
        Integer starIndex = (pageNum - 1) * pageSize;
        return subjectAreaServiceExteror.getSubjectAreaList(subjectName,status,starIndex,pageSize);
    }



    /**
     * 修改主题领域状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/updateSubjectAreaStatus")
    public Result updateSubjectAreaStatus(@RequestParam Long id,@RequestParam Integer status){
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
        return subjectAreaServiceExteror.updateSubjectAreaStatus(id,status);
    }



    /**
     * 删除主题领域
     * @param id
     * @return
     */
    @PostMapping("/delSubjectArea")
    public Result delSubjectArea(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }
        return subjectAreaServiceExteror.delSubjectArea(id);
    }

    /**
     * 添加主题领域
     * @param subjectName
     * @return
     */
    @PostMapping("/addSubjectArea")
    public Result addSubjectArea(@RequestParam String subjectName){
        if(null==subjectName|| StringUtils.isEmpty(subjectName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写主题领域!").code("7000").build();
        }else {
            subjectName=subjectName.replace(" ","");
        }

        return subjectAreaServiceExteror.addSubjectArea(subjectName);
    }


    /**
     * 修改主题领域
     * @param subjectName
     * @return
     */
    @PostMapping("/updateSubjectArea")
    public Result updateSubjectArea(@RequestParam Long id,@RequestParam String subjectName){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择数据!").code("7000").build();
        }
        if(null==subjectName|| StringUtils.isEmpty(subjectName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写主题领域!").code("7000").build();
        }else {
            subjectName=subjectName.replace(" ","");
        }

        return subjectAreaServiceExteror.updateSubjectArea(id,subjectName);
    }
}
