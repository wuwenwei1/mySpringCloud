package org.demo.security.common.web.model;


public final class ResultBuilder {

  private Result result;

  private ResultBuilder() {
    result = new Result();
  }

  public static ResultBuilder aResult() {
    return new ResultBuilder();
  }

  public ResultBuilder code(String code) {
    result.setCode(code);
    return this;
  }

  public ResultBuilder msg(String msg) {
    result.setMessage(msg);
    return this;
  }

  public <T> ResultBuilder data(T data) {
    result.setData(data);
    return this;
  }

  public Result build() {
    return result;
  }
}
