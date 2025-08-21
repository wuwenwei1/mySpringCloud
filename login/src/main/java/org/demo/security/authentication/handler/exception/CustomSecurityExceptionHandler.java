package org.demo.security.authentication.handler.exception;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.demo.security.common.web.exception.BaseException;
import org.demo.security.common.web.model.ResultBuilder;

import org.demo.security.common.web.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 项目中全局的异常处理,全局中出了异常都会执行这个类中的方法,将原因告知客户端
 */
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {



  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (BaseException e) {
      // 自定义异常
      Result result = ResultBuilder.aResult()
          .msg(e.getMessage())
          .code(e.getCode())
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.OK.value());
      PrintWriter writer = response.getWriter();
      writer.write(JSONObject.toJSONString(result));
      writer.flush();
      writer.close();
    } catch (AuthenticationException | AccessDeniedException e) {
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.OK.value());
      PrintWriter writer = response.getWriter();

      writer.print(JSONObject.toJSONString(ResultBuilder.aResult()
              .msg(e.getMessage())
              .code("5000")
              .build()));
      writer.flush();
      writer.close();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      // 未知异常
      Result result = ResultBuilder.aResult()
          .msg("未知异常")
          .code("5000")
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.OK.value());
      PrintWriter writer = response.getWriter();
      writer.write(JSONObject.toJSONString(result));
      writer.flush();
      writer.close();
    }
  }
}
