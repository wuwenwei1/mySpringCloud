package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.service.DataRemittanceReviewFileServiceExterior;
import org.demo.security.service.UsingApplyFileServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/DataRemittanceReviewFile")
public class DataRemittanceReviewFileController {

    @Autowired
    private DataRemittanceReviewFileServiceExterior dataRemittanceReviewFileServiceExterior;
    /**
     * 查看汇交数据副本的文件
     * @param id
     * @return
     */
    @PostMapping("/getDataRemittanceReviewFileList")
    public Result getDataRemittanceReviewFileList(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return dataRemittanceReviewFileServiceExterior.getDataRemittanceReviewFileList(id);
    }

//////////////////////////////////////

}
