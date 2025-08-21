package org.demo.security.entity;

import lombok.Data;

/**
 * role_usertype
 * @author 
 */
@Data
public class RoleUserType {

    private Long roleUserTypeId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户类型ID
     */
    private Long userTypeId;


}