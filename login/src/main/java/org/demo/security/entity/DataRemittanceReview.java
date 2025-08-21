package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * data_remittance_review
 * @author 
 */
@Data
public class DataRemittanceReview {
    private Long id;

    /**
     * 汇交数据Id
     */
    private Long remittanceId;

    /**
     * 汇交数据名称
     */
    private String dataName;

    /**
     * 行业分类ID
     */
    private Long industryTypeId;

    /**
     * 主题领域ID
     */
    private Long subjectAreaId;

    /**
     * 数据开放类型ID
     */
    private Long dataOpenTypeId;

    /**
     * 审核类型Id
     */
    private Long reviewTypeId;

    /**
     * 汇交方
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * 描述
     */
    private String remark;

    /**
     * 汇交类型(0:新增,1:更新,2:重提)
     */
    private String remittanceType;




}