package org.demo.security.common.web.exception;

import org.springframework.http.HttpStatus;

/**
 * 抛异常的工具类
 */
public class ExceptionTool {

  private static final HttpStatus defaultHttpStatus = HttpStatus.OK;


  public static void throwException(String message, String code) {
    throw new BaseException(message, code);
  }

}
