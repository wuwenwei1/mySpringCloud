package org.demo.security.common.web.service;

import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.RoleMapper;
import org.demo.security.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author ruoyi
 */
@Service("ss")
public class PermissionService {

    @Autowired
    private RoleMapper roleMapper;
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";


    /**
     * 验证用户是否登录了
     */
    public boolean hasPermi(String permission) {
        Long userId = SecurityUtils.getUserId();
        Role role = roleMapper.getRoleByUserId(userId);
        if (null == role || "1".equals(role.getDelFlag()) ||"1".equals(role.getStatus())) {
            ExceptionTool.throwException("您无任何权限!", "5000");
        }

        if (SecurityUtils.isAdmin(role.getRoleId())) {
            return true;
        } else {
            //如果不是超级管理员，就查询是否有权限
        }


        return false;
    }


    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    /*public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(), permission);
    }*/

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
   /* public boolean lacksPermi(String permission)
    {
        return hasPermi(permission) != true;
    }*/

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    /*public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isEmpty(permissions))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }

        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER))
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }*/


}
