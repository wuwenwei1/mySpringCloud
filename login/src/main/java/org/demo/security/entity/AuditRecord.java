package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * audit_record
 * @author 
 */
@Data
public class AuditRecord{
    private Long id;

    /**
     * 汇交审核数据Id
     */
    private Long reviewId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 审核人
     */
    private Long reviewBy;

    /**
     * 审核意见
     */
    private String reviewIdea;


}