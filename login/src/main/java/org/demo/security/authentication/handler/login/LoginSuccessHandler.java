package org.demo.security.authentication.handler.login;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.demo.security.authentication.config.RedisCache;
import org.demo.security.authentication.config.TokenService;
import org.demo.security.common.constant.TencentCloudConstant;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


/**
 * 认证成功/登录成功 事件处理器
 */
@Component
public class LoginSuccessHandler extends
    AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private TokenService tokenService;
  @Autowired
  private RedisCache redisCache;

  @Autowired
  private UserMapper userMapper;
  public LoginSuccessHandler() {
    this.setRedirectStrategy(new RedirectStrategy() {
      @Override
      public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
          throws IOException {
        // 更改重定向策略，前后端分离项目，后端使用RestFul风格，无需做重定向
        // Do nothing, no redirects in REST
      }
    });
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    Object principal = authentication.getPrincipal();
    authentication.getCredentials();
    //登陆认证成功后，authentication.getPrincipal()返回的Object对象必须是：LoginUser
    if (principal == null || !(principal instanceof LoginUser)) {
      ExceptionTool.throwException("系统异常","5000");
    }

    LoginUser currentUser = (LoginUser) principal;
    // 生成token和refreshToken
    Map<String, Object> responseData = new LinkedHashMap<>();
    responseData.put("token", tokenService.createToken(currentUser));
    responseData.put("refreshToken", tokenService.createToken(currentUser));

    // 一些特殊的登录参数。比如三方登录，需要额外返回一个字段是否需要跳转的绑定已有账号页面
    Object details = authentication.getDetails();
    if (details instanceof Map) {
      Map detailsMap = (Map)details;
      String loginType = (String) detailsMap.get("loginType");
      if(null!=loginType&& StringUtils.isNotEmpty(loginType)&&"smsLogin".equals(loginType)){
        redisCache.deleteObject(TencentCloudConstant.SMS_LOGIN_PRE + currentUser.getUser().getPhone());
      }
    }

    userMapper.updateLoginDateById(currentUser.getUserId(), DataConversionUtil.getTodayAllStr(new Date()));

    // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    PrintWriter writer = response.getWriter();
    writer.print(JSONObject.toJSONString(Result.data(responseData, "登录成功!")));
    writer.flush();
    writer.close();
  }



}
