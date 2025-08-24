package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.DataRemittance;
import org.demo.security.entity.DataRemittanceReview;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface DataRemittanceMapper {

    List<JSONObject> getDataRemittanceList(@Param("dataName")String dataName,
                                           @Param("industryTypeId")Long industryTypeId,
                                           @Param("subjectAreaId")Long subjectAreaId,
                                           @Param("dataOpenTypeId")Long dataOpenTypeId,
                                           @Param("reviewTypeId")Long reviewTypeId,
                                           @Param("createStartTime")String createStartTime,
                                           @Param("createEndTime")String createEndTime,
                                           @Param("starIndex")Integer starIndex,
                                           @Param("pageSize")Integer pageSize);

    Long getDataRemittanceTotal(@Param("dataName")String dataName,
                                @Param("industryTypeId")Long industryTypeId,
                                @Param("subjectAreaId")Long subjectAreaId,
                                @Param("dataOpenTypeId")Long dataOpenTypeId,
                                @Param("reviewTypeId")Long reviewTypeId,
                                @Param("createStartTime")String createStartTime,
                                @Param("createEndTime")String createEndTime);
    void updateDataRemittanceById(@Param("dataRemittance")DataRemittance dataRemittance);
    void addDataRemittance(@Param("dataRemittance") DataRemittance dataRemittance);

    DataRemittance getDataRemittanceByIdAndLockIn(@Param("id")Long id);


    void updateReviewTypeIdById(@Param("id")Long id,@Param("reviewTypeId") Long reviewTypeId);



    JSONObject getDataRemittanceById(@Param("id")Long id);

    DataRemittance getDataRemittanceObjectById(@Param("id")Long id);

    void updateIsShareById(@Param("id")Long id,@Param("isShare") Integer isShare,@Param("updateBy") Long updateBy,@Param("updateTime") String updateTime);

////////////////////////////////////













}