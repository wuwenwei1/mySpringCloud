package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * role_menu
 * @author 
 */
@Data
public class RoleMenu  {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;


}