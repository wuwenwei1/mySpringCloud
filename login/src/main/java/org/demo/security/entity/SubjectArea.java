package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * subject_area
 * @author 
 */
@Data
public class SubjectArea  {
    private Long id;

    /**
     * 主题领域名称
     */
    private String subjectName;

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