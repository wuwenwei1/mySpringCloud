package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.IndustryTypeMapper;
import org.demo.security.entity.IndustryType;
import org.demo.security.service.IndustryTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class IndustryTypeServiceExterorImpl implements IndustryTypeServiceExteror {
    @Autowired
    private IndustryTypeMapper industryTypeMapper;

    /**
     * 添加行业名称
     * @param industryName
     * @return
     */
    @Transactional
    @Override
    public Result addIndustryType(String industryName) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        IndustryType industryType = new IndustryType();
        industryType.setIndustryName(industryName);
        industryType.setStatus("0");
        industryType.setDelFlag("0");
        industryType.setCreateBy(userId);
        industryType.setCreateTime(todayAllStr);
        industryType.setUpdateBy(userId);
        industryType.setUpdateTime(todayAllStr);
        try {
            industryTypeMapper.addIndustryType(industryType);
        }catch (Exception e){
            ExceptionTool.throwException("添加异常","7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();

    }

    /**
     * 修改行业名称
     * @param id
     * @param industryName
     * @return
     */
    @Transactional
    @Override
    public Result updateIndustryType(Long id, String industryName) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            industryTypeMapper.updateIndustryNameById(id,industryName,userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("修改异常","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }

    /**
     * 删除行业名称
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result delIndustryType(Long id) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            industryTypeMapper.delIndustryTypeById(id,userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("删除异常!","7000");
        }
        return ResultBuilder.aResult().msg("删除成功").code("2000").build();
    }

    /**
     * 修改行业名称状态
     * @param id
     * @param status
     * @return
     */
    @Transactional
    @Override
    public Result updateIndustryTypeStatus(Long id, Integer status) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        try {
            industryTypeMapper.updateIndustryTypeStatusById(id,String.valueOf(status),userId,todayAllStr);
        }catch (Exception e){
            ExceptionTool.throwException("修改异常","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }

    /**
     * 行业名称管理列表
     * @param industryName
     * @param status
     * @param starIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public Result getIndustryTypeList(String industryName, Integer status, Integer starIndex, Integer pageSize) {
        String statusStr=null;
        if(null==status){
            statusStr="";
        }else {
            statusStr=String.valueOf(status);
        }

        List<JSONObject> industryTypes=industryTypeMapper.getIndustryTypeList(industryName,statusStr,starIndex,pageSize);
        return ResultBuilder.aResult().data(industryTypes).code("2000").build();
    }

    /**
     * 行业名称下拉框
     * @return
     */
    @Transactional
    @Override
    public Result getIndustryTypeBox() {
        List<JSONObject> industryTypes=industryTypeMapper.getIndustryTypeBox();
        return ResultBuilder.aResult().data(industryTypes).code("2000").build();
    }
}
