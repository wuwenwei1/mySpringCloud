package org.demo.security.common.web.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class WebGlobalExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(value = Exception.class)
  public Result exceptionHandler(HttpServletResponse response, Exception e) {
    response.setStatus(HttpStatus.OK.value());
    logger.info("服务器异常", e);
    return Result.fail("服务器异常");
  }

  @ExceptionHandler(value = NoResourceFoundException.class)
  public Result exceptionHandler(HttpServletResponse response, NoResourceFoundException e) {
    response.setStatus(HttpStatus.OK.value());
    return ResultBuilder.aResult()
        .msg("api not found")
        .code("api.not.found")
        .build();
  }
  @ExceptionHandler(value = BaseException.class)
  public Result exceptionHandler(HttpServletResponse response, BaseException e) {
    response.setStatus(HttpStatus.OK.value());
    return createResult(e);
  }

  private Result createResult(BaseException e) {
    return ResultBuilder.aResult()
        .msg(e.getMessage())
        .code(e.getCode())
        .build();
  }
}
