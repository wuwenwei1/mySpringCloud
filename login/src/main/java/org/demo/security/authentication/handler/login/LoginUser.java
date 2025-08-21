package org.demo.security.authentication.handler.login;

import lombok.Data;
import org.demo.security.entity.Role;
import org.demo.security.entity.User;

import java.io.Serializable;

/**
 * 用户信息登陆后的信息，会序列化到Jwt的payload
 * 
 */
@Data
public class LoginUser implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 随机生成的uuid
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 用户角色
     */
    private Role role;


}
