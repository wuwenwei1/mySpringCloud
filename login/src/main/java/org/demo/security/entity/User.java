package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Long userId;

    /**
     * 用户名(法人注册时就是法人姓名)
     */
    private String userName;

    /**
     * 手机
     */
    private String phone;

    /**
     * 密码
     */
    private String password;


    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 添加时间(yyyy-MM-dd HH:mm:ss)
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
     * 用户头像路径
     */
    private String userImage;


    /**
     * 最后登录时间(yyyy-MM-dd HH:mm:ss)
     */
    private String loginDate;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 用户类型（1 计量院管理 2普通类型 3个人注册 4企业注册）
     */
    private Long userTypeId;

}