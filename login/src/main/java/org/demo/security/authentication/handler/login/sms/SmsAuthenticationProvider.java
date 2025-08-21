package org.demo.security.authentication.handler.login.sms;

import org.demo.security.authentication.config.RedisCache;
import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.common.constant.TencentCloudConstant;
import org.demo.security.common.web.exception.BaseException;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.dao.RoleMapper;
import org.demo.security.dao.UserMapper;
import org.demo.security.entity.Role;
import org.demo.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private RedisCache redisCache;

  @Autowired
  private RoleMapper roleMapper;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String phoneNumber = authentication.getPrincipal().toString();
    String smsLoginCaptcha = authentication.getCredentials().toString();


    // 验证验证码是否正确
    if (!(Long.parseLong(smsLoginCaptcha) >= 10000 && Long.parseLong(smsLoginCaptcha) < 100000)) {
      ExceptionTool.throwException("注册码无效!","5000");
    } else {
      try {
        boolean b = redisCache.hasKey(TencentCloudConstant.SMS_LOGIN_PRE + phoneNumber);
        if (!b) {
          ExceptionTool.throwException("登录码过期!","5000");
        } else {
          Long cacheCode = redisCache.getCacheObject(TencentCloudConstant.SMS_LOGIN_PRE + phoneNumber);
          if (!(cacheCode.equals(Long.parseLong(smsLoginCaptcha)))) {
            ExceptionTool.throwException("登录码过期!","5000");
          }
        }
      }catch (Exception e){
        if(e instanceof BaseException){
          ExceptionTool.throwException(e.getMessage(),((BaseException) e).getCode());
        }
        ExceptionTool.throwException("系统错误!","5000");
      }
    }

    // 根据手机号查询用户信息
    User user = userMapper.getUserByPhone(phoneNumber);
    if (user == null) {
      ExceptionTool.throwException("手机用户不存在","5000");
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
    SmsAuthentication token = new SmsAuthentication();

    Map<String, Object> DetailsMao = new HashMap<>();
    DetailsMao.put("loginType","smsLogin");
    token.setDetails(DetailsMao);

    token.setCurrentUser(loginUser);
    token.setAuthenticated(true); // 认证通过，这里一定要设成true
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SmsAuthentication.class.isAssignableFrom(authentication);
  }


}