package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.SubjectAreaMapper;
import org.demo.security.entity.SubjectArea;
import org.demo.security.service.SubjectAreaServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SubjectAreaServiceExterorImpl implements SubjectAreaServiceExteror {
    @Autowired
    private SubjectAreaMapper subjectAreaMapper;

    /**
     * 添加主题领域
     * @param subjectName
     * @return
     */
    @Transactional
    @Override
    public Result addSubjectArea(String subjectName) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setSubjectName(subjectName);
        subjectArea.setStatus("0");
        subjectArea.setDelFlag("0");
        subjectArea.setCreateBy(userId);
        subjectArea.setCreateTime(todayAllStr);
        subjectArea.setUpdateBy(userId);
        subjectArea.setUpdateTime(todayAllStr);
        try {
            subjectAreaMapper.addSubjectArea(subjectArea);
        }catch (Exception e){
            ExceptionTool.throwException("添加异常","7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();

    }

    /**
     * 修改主题领域
     * @param id
     * @param subjectName
     * @return
     */
    @Transactional
    @Override
    public Result updateSubjectArea(Long id, String subjectName) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            subjectAreaMapper.updateSubjectNameById(id,subjectName,userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("修改异常","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }

    /**
     * 删除主题领域
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result delSubjectArea(Long id) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            subjectAreaMapper.delSubjectAreaById(id,userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("删除异常!","7000");
        }
        return ResultBuilder.aResult().msg("删除成功").code("2000").build();
    }

    /**
     * 修改主题领域状态
     * @param id
     * @param status
     * @return
     */
    @Transactional
    @Override
    public Result updateSubjectAreaStatus(Long id, Integer status) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            subjectAreaMapper.updateSubjectAreaStatusById(id,String.valueOf(status),userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("修改异常","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }

    /**
     * 主题领域管理列表
     * @param subjectName
     * @param status
     * @param starIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public Result getSubjectAreaList(String subjectName, Integer status, Integer starIndex, Integer pageSize) {
        String statusStr=null;
        if(null==status){
            statusStr="";
        }else {
            statusStr=String.valueOf(status);
        }

        List<JSONObject> subjectAreas=subjectAreaMapper.getSubjectAreaList(subjectName,statusStr,starIndex,pageSize);
        return ResultBuilder.aResult().data(subjectAreas).code("2000").build();
    }

    /**
     * 主题领域下拉框
     * @return
     */
    @Transactional
    @Override
    public Result getSubjectAreaBox() {
        List<JSONObject> subjectAreas=subjectAreaMapper.getSubjectAreaBox();
        return ResultBuilder.aResult().data(subjectAreas).code("2000").build();
    }
}
