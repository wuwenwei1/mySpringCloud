package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.IndustryType;
import org.demo.security.entity.SubjectArea;

import java.util.List;

public interface SubjectAreaMapper {
    void addSubjectArea(@Param("subjectArea") SubjectArea subjectArea);

    void updateSubjectNameById(@Param("id")Long id,@Param("subjectName")String subjectName, @Param("userId")Long userId,@Param("todayAllStr") String todayAllStr);

    void delSubjectAreaById(@Param("id")Long id, @Param("userId")Long userId,@Param("todayAllStr") String todayAllStr);

    void updateSubjectAreaStatusById(@Param("id")Long id,@Param("status") String status,@Param("userId") Long userId,@Param("todayAllStr") String todayAllStr);

    List<JSONObject> getSubjectAreaList(@Param("subjectName")String subjectName, @Param("status") String status, @Param("starIndex") Integer starIndex, @Param("pageSize") Integer pageSize);

    List<JSONObject> getSubjectAreaBox();

}