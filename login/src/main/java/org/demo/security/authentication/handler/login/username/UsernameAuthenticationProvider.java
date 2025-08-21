package org.demo.security.authentication.handler.login.username;

import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.common.web.exception.ExceptionTool;

import org.demo.security.dao.RoleMapper;
import org.demo.security.dao.UserMapper;
import org.demo.security.entity.Role;
import org.demo.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 帐号密码登录认证
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public UsernameAuthenticationProvider() {
    super();
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 用户提交的用户名 + 密码：
    String username = (String)authentication.getPrincipal();
    String password = (String) authentication.getCredentials();
    User user=userMapper.getUserByUserName(username);
    if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
      ExceptionTool.throwException("用户名或密码不正确","5000");
    }
    if("1".equals(user.getDelFlag())){
      ExceptionTool.throwException("用户账号已被剔除","5000");
    }
    if("1".equals(user.getStatus())){
      ExceptionTool.throwException("用户账号已被禁用","5000");
    }
    user.setPassword(null);
    Role role = roleMapper.getRoleByUserId(user.getUserId());
    LoginUser loginUser = new LoginUser();
    loginUser.setUserId(user.getUserId());
    loginUser.setUser(user);
    loginUser.setRole(role);
    UsernameAuthentication token = new UsernameAuthentication();
    token.setCurrentUser(loginUser);
    token.setAuthenticated(true); // 认证通过，这里一定要设成true
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(UsernameAuthentication.class);
  }
}

