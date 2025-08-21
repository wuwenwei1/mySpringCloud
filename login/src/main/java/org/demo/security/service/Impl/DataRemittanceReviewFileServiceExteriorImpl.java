package org.demo.security.service.Impl;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.dao.DataRemittanceReviewFileMapper;
import org.demo.security.entity.DataRemittanceReviewFile;
import org.demo.security.entity.UsingApplyFile;
import org.demo.security.service.DataRemittanceReviewFileServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataRemittanceReviewFileServiceExteriorImpl implements DataRemittanceReviewFileServiceExterior {
    @Autowired
    private DataRemittanceReviewFileMapper dataRemittanceReviewFileMapper;
    /**
     * 查看汇交数据副本的文件
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReviewFileList(Long id) {
        List<DataRemittanceReviewFile> dataRemittanceReviewFileList=dataRemittanceReviewFileMapper.getDataRemittanceReviewFileListByReviewId(id);
        return ResultBuilder.aResult().code("2000").data(dataRemittanceReviewFileList).build();
    }
}
