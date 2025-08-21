package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.IndustryType;

import java.util.List;

public interface IndustryTypeMapper {

    void addIndustryType(@Param("industryType") IndustryType industryType);

    IndustryType getIndustryTypeAllColumnById(String id);

    void updateIndustryNameById(@Param("id")Long id,@Param("industryName")String industryName, @Param("userId")Long userId,@Param("todayAllStr") String todayAllStr);

    void delIndustryTypeById(@Param("id")Long id, @Param("userId")Long userId,@Param("todayAllStr") String todayAllStr);

    void updateIndustryTypeStatusById(@Param("id")Long id,@Param("status") String status,@Param("userId") Long userId,@Param("todayAllStr") String todayAllStr);

    List<JSONObject> getIndustryTypeList(@Param("industryName")String industryName,@Param("status") String status,@Param("starIndex") Integer starIndex,@Param("pageSize") Integer pageSize);

    List<JSONObject> getIndustryTypeBox();
}