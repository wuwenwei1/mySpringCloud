package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * data_open_type
 * @author 
 */
@Data
public class DataOpenType {
    private Long id;

    /**
     * 数据开放类型名称
     */
    private String dataOpenTypeName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建者
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


}