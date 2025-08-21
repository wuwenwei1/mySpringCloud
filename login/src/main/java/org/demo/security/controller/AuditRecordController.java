package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.service.AuditRecordServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/AuditRecord")
public class AuditRecordController {

    @Autowired
    private AuditRecordServiceExterior auditRecordServiceExterior;

    /**
     * 查看汇交数据的审核意见列表
     * @param id
     * @return
     */
    @PostMapping("/getReviewsIdeaList")
    public Result getReviewsIdeaList(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择查看的数据!").code("7000").build();
        }
        return auditRecordServiceExterior.getAuditRecordList(id);
    }
/////////////////////////////////////////////////////
    /**
     * 删除汇交数据的审核意见
     * @param id
     * @return
     */
    @PostMapping("/delReviewsIdea")
    public Result delReviewsIdea(@RequestParam Long id){
        if(null==id){
            return ResultBuilder.aResult().msg("请选择删除的数据!").code("7000").build();
        }
        return auditRecordServiceExterior.delReviewsIdea(id);
    }
}
