package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.service.DataOpenTypeServiceExteror;
import org.demo.security.service.ReviewTypeServiceExteror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ReviewType")
public class ReviewTypeController {
    @Autowired
    private ReviewTypeServiceExteror reviewTypeServiceExteror;


    /**
     * 审核类型下拉框
     * @return
     */
    @PostMapping("/getReviewTypeBox")
    public Result getReviewTypeBox(){
        return reviewTypeServiceExteror.getReviewTypeBox();
    }




}
