package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * review_type
 * @author 
 */
@Data
public class ReviewType  {
    private Long id;

    /**
     * 审核类型名称
     */
    private String reviewName;

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