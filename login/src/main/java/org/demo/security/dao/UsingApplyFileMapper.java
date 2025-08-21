package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.UsingApplyFile;

import java.util.List;

public interface UsingApplyFileMapper {

    void addUsingApplyFile(@Param("usingApplyFile") UsingApplyFile usingApplyFile);

    void updateUsingApplyFileById(@Param("usingApplyFile")UsingApplyFile usingApplyFile);

    List<UsingApplyFile> getUsingApplyFileListByUsingApplyId(@Param("usingApplyId")Long usingApplyId);


}