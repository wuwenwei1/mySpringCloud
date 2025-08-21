package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * user_role
 * @author 
 */
@Data
public class UserRole  {

    private Long id;
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 用户id
     */
    private Long userId;


}