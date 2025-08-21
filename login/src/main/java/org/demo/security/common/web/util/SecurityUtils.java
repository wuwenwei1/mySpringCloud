package org.demo.security.common.web.util;

import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.common.web.exception.ExceptionTool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{
    /**
     * 用户ID
     **/
    public static Long getUserId()
    {
        if(null==getLoginUser()){
            return null;
        }
        LoginUser loginUser = getLoginUser();
        return loginUser.getUserId();
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        if(null==getAuthentication()){
            ExceptionTool.throwException("请先登录!","5000");
        }
        return (LoginUser) getAuthentication().getPrincipal();
    }


    
    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        if(null==getLoginUser()){
            return null;
        }
        return getLoginUser().getUser().getUserName();
    }



    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication()!=null?SecurityContextHolder.getContext().getAuthentication():null;
    }





    /**
     * 是否为管理员
     * 
     * @param roleId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }
}
