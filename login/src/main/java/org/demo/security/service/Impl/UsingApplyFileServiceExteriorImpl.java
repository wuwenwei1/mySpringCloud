package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.dao.UsingApplyFileMapper;
import org.demo.security.entity.UsingApplyFile;
import org.demo.security.service.UsingApplyFileServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsingApplyFileServiceExteriorImpl implements UsingApplyFileServiceExterior {
    @Autowired
    private UsingApplyFileMapper usingApplyFileMapper;

    /**
     * 查看数据使用申请的文件
     * @param usingApplyId
     * @return
     */
    @Transactional
    @Override
    public Result getUsingApplyFileList(Long usingApplyId) {
        List<UsingApplyFile> usingApplyFiles=usingApplyFileMapper.getUsingApplyFileListByUsingApplyId(usingApplyId);
        return ResultBuilder.aResult().code("2000").data(usingApplyFiles).build();
    }


}
