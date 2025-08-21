package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.dao.DataOpenTypeMapper;
import org.demo.security.dao.ReviewTypeMapper;
import org.demo.security.service.DataOpenTypeServiceExteror;
import org.demo.security.service.ReviewTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewTypeServiceExterorImpl implements ReviewTypeServiceExteror {
    @Autowired
    private ReviewTypeMapper reviewTypeMapper;

    /**
     * 审核类型下拉框
     * @return
     */
    @Transactional
    @Override
    public Result getReviewTypeBox() {
        List<JSONObject> industryTypes=reviewTypeMapper.getReviewTypeBox();
        return ResultBuilder.aResult().data(industryTypes).code("2000").build();
    }
}
