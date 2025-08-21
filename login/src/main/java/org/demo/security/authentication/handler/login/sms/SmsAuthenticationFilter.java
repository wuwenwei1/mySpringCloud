package org.demo.security.authentication.handler.login.sms;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger logger = LoggerFactory.getLogger(SmsAuthenticationFilter.class);

  public SmsAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
      AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler) {
    super(pathRequestMatcher);
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(authenticationSuccessHandler);
    setAuthenticationFailureHandler(authenticationFailureHandler);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    logger.debug("user SmsCodeAuthenticationFilter");

    // 提取请求数据
    String requestJsonData = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    Map<String, Object> requestMapData= JSONObject.parseObject(requestJsonData);
    String phoneNumber = requestMapData.get("phone").toString();
    String smsLoginCaptcha = requestMapData.get("smsLoginCaptcha").toString();
    if(null==phoneNumber || StringUtils.isEmpty(phoneNumber.replace(" ",""))){
      ExceptionTool.throwException("请填写手机号","5000");
    }
    if(null==smsLoginCaptcha || StringUtils.isEmpty(smsLoginCaptcha.replace(" ",""))){
      ExceptionTool.throwException("请填写验证码","5000");
    }










    SmsAuthentication authentication = new SmsAuthentication();
    authentication.setPhone(phoneNumber);
    authentication.setSmsCode(smsLoginCaptcha);
    authentication.setAuthenticated(false); // 提取参数阶段，authenticated一定是false
    return this.getAuthenticationManager().authenticate(authentication);
  }

}