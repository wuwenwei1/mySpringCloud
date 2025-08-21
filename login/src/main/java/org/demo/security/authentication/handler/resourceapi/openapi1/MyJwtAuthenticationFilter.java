package org.demo.security.authentication.handler.resourceapi.openapi1;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.authentication.handler.login.username.UsernameAuthentication;
import org.demo.security.authentication.config.TokenService;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.dao.UserMapper;
import org.demo.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//@Component
public class MyJwtAuthenticationFilter extends OncePerRequestFilter {


  @Autowired
  private UserMapper userMapper;

  private TokenService tokenService;

  public MyJwtAuthenticationFilter(TokenService tokenService,UserMapper userMapper) {
    this.tokenService = tokenService;
    this.userMapper = userMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
      logger.debug("Use OpenApi1AuthenticationFilter");
    LoginUser loginUser = tokenService.getLoginUser(request);
    if(null==loginUser){
      ExceptionTool.throwException("登录过期", "6000");
    }else {

      User user = userMapper.getUserByUserId(loginUser.getUserId());
      if("1".equals(user.getDelFlag())){
        ExceptionTool.throwException("用户已被剔除","5000");
      }
      if("1".equals(user.getStatus())){
        ExceptionTool.throwException("用户已被禁用","5000");
      }
      tokenService.verifyToken(loginUser);
      UsernameAuthentication authentication = new UsernameAuthentication();
      authentication.setCurrentUser(tokenService.getLoginUser(request));
      authentication.setAuthenticated(true); // 认证通过，这里一定要设成true
      // 认证通过后，一定要设置到SecurityContextHolder里面去。
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 放行
    filterChain.doFilter(request, response);
  }
}
