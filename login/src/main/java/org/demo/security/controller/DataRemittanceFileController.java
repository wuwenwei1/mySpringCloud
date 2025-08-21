package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.DataRemittanceFileServiceExterior;
import org.demo.security.service.DataRemittanceServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/DataRemittanceFile")
public class DataRemittanceFileController {

    @Autowired
    private DataRemittanceFileServiceExterior dataRemittanceFileServiceExterior;
    /**
     * 查看汇交数据的文件列表
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceFileList")
    public Result getDataRemittanceFileList(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceFileServiceExterior.getDataRemittanceFileList(id);
    }

///////////////////////////////////////////
}
