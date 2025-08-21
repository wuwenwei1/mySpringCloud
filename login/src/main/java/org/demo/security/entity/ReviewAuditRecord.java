package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * review_audit_record
 * @author 
 */
@Data
public class ReviewAuditRecord {
    private Long id;

    /**
     * 汇交数据副本Id
     */
    private Long reviewId;

    /**
     * 汇交审核数据Id
     */
    private Long auditRecordId;


}