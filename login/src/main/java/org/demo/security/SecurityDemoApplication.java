package org.demo.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "org.demo.security.dao")
public class SecurityDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityDemoApplication.class, args);
  }
}