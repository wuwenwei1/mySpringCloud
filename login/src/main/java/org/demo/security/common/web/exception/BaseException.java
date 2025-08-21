package org.demo.security.common.web.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

  private static final long serialVersionUID = -7972131521045668011L;
  private String code; // 自定义一个全局唯一的code，

  public BaseException(String message, String code) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}