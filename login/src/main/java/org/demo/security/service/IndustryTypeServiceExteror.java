package org.demo.security.service;

import org.demo.security.common.web.model.Result;

public interface IndustryTypeServiceExteror {
    Result addIndustryType(String industryName);

    Result updateIndustryType(Long id, String industryName);

    Result delIndustryType(Long id);

    Result updateIndustryTypeStatus(Long id, Integer status);

    Result getIndustryTypeList(String industryName, Integer status, Integer starIndex, Integer pageSize);

    Result getIndustryTypeBox();
}
