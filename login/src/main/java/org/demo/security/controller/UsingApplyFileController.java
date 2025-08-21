package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.UsingApplyFileServiceExterior;
import org.demo.security.service.UsingApplyServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/UsingApplyFile")
public class UsingApplyFileController {

    @Autowired
    private UsingApplyFileServiceExterior usingApplyFileServiceExterior;
    /**
     * 查看数据使用申请的文件
     * @param usingApplyId
     * @return
     */
    @PostMapping("/getUsingApplyFileList")
    public Result getUsingApplyFileList(@RequestParam Long usingApplyId){
        if(null==usingApplyId){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return usingApplyFileServiceExterior.getUsingApplyFileList(usingApplyId);
    }

///////////////////////////////

}
