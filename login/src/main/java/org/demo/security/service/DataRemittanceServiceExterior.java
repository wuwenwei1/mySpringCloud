package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataRemittanceServiceExterior {
    Result addDataRemittance(String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files);

    Result getDataRemittanceReviewData(Long id);
    ///////////////////////////////////////
    Result getDataRemittance(Long id);

    Result afreshAddDataRemittance(Long id, String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files);

    Result updateDataRemittance(Long id, String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files);


    Result getDataRemittanceList(String dataName, Long industryTypeId, Long subjectAreaId, Long dataOpenTypeId, Long reviewTypeId, String createStartTime, String createEndTime, Integer starIndex,Integer pageSize);

    Result updateShareById(Long id, Integer isShare);
}
