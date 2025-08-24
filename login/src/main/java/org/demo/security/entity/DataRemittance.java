package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * data_remittance
 * @author 
 */
@Data
public class DataRemittance {
    private Long id;

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
     * 是否共享(1:共享，2:不共享),默认1
     */
    private Integer isShare;

    /**
     * 汇交类型ID
     */
    private Long remittanceTypeId;

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


}