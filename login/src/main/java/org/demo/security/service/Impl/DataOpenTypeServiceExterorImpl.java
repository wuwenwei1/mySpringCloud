package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.DataOpenTypeMapper;
import org.demo.security.entity.IndustryType;
import org.demo.security.service.DataOpenTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DataOpenTypeServiceExterorImpl implements DataOpenTypeServiceExteror {
    @Autowired
    private DataOpenTypeMapper dataOpenTypeMapper;

    /**
     * 数据开放类型下拉框
     * @return
     */
    @Transactional
    @Override
    public Result getDataOpenTypeBox() {
        List<JSONObject> industryTypes=dataOpenTypeMapper.getDataOpenTypeBox();
        return ResultBuilder.aResult().data(industryTypes).code("2000").build();
    }
}
