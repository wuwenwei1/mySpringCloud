package org.demo.security.common.web.util;

import org.springframework.context.ApplicationContext;

public class SpringBeanTool {

  private static ApplicationContext context;

  public static ApplicationContext setApplicationContext(ApplicationContext context) {
    return context;
  }

  public static ApplicationContext getApplicationContext() {
    return context;
  }
}