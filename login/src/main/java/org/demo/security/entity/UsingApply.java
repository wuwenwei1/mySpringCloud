package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * using_apply
 * @author 
 */
@Data
public class UsingApply  {
    private Long id;

    /**
     * 汇交数据Id
     */
    private Long remittanceId;

    /**
     * 申请人
     */
    private Long applyBy;

    /**
     * 申请时间
     */
    private String applyTime;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 使用场景
     */
    private String usageScenarios;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * 审核类型Id
     */
    private Long reviewTypeId;

    /**
     * 审核人
     */
    private Long reviewBy;

    /**
     * 审核时间
     */
    private String reviewTime;

    /**
     * 审核意见
     */
    private String reviewIdea;


}