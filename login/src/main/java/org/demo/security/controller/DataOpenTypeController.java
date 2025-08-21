package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.DataOpenTypeServiceExteror;
import org.demo.security.service.IndustryTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/DataOpenType")
public class DataOpenTypeController {
    @Autowired
    private DataOpenTypeServiceExteror dataOpenTypeServiceExteror;


    /**
     * 数据开放类型下拉框
     * @return
     */
    @PostMapping("/getDataOpenTypeBox")
    public Result getDataOpenTypeBox(){
        return dataOpenTypeServiceExteror.getDataOpenTypeBox();
    }




}
