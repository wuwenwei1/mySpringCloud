package org.demo.security.authentication.handler.exception;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 认证失败时，会执行这个类中的方法。将原因告知客户端
 */
public class CustomAuthenticationExceptionHandler implements
    AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.setStatus(HttpStatus.OK.value());
    PrintWriter writer = response.getWriter();
    writer.print(JSONObject.toJSONString(ResultBuilder.aResult().msg("认证失败").code("5000")));
    writer.flush();
    writer.close();
  }
}
