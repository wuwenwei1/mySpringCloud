package org.demo.security.service.Impl;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.dao.DataRemittanceFileMapper;
import org.demo.security.entity.DataRemittanceFile;
import org.demo.security.entity.UsingApplyFile;
import org.demo.security.service.DataRemittanceFileServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataRemittanceFileServiceExteriorImpl implements DataRemittanceFileServiceExterior {
    @Autowired
    private DataRemittanceFileMapper dataRemittanceFileMapper;


    /**
     * 查看汇交数据的文件列表
     * @param remittanceId
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceFileList(Long remittanceId) {
        List<DataRemittanceFile> dataRemittanceFiles=dataRemittanceFileMapper.getDataRemittanceFileListByRemittanceId(remittanceId);
        return ResultBuilder.aResult().code("2000").data(dataRemittanceFiles).build();
    }
}
