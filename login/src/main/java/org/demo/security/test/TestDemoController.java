package org.demo.security.test;


import org.demo.security.authentication.handler.login.LoginUser;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/open-api")
public class TestDemoController {


  @GetMapping("/business-1")
  public Result getA() {

    /*LoginUser userLoginInfo = (LoginUser) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();*/
    return ResultBuilder.aResult()
        //.data(userLoginInfo)
        .build();
  }








}
