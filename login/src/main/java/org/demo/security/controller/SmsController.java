package org.demo.security.controller;

import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.MyPatternUtil;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/Sms")
public class SmsController {
    @Autowired
    private SmsService smsService;

    /**
     * 忘记密码时，获取修改密码需要的验证码
     * @param phone
     * @return
     */
    @PostMapping("/getForgetCaptcha")
    public Result getForgetCaptcha(@RequestParam String phone) {
        if (null == phone || StringUtils.isEmpty(phone.replaceAll(" ", ""))) {
            ExceptionTool.throwException("请填写手机号!","7000");
        }else {
            phone = phone.replaceAll(" ", "");
            boolean isPhone = MyPatternUtil.validatePhoneNumber(phone);
            if (isPhone) {
                return smsService.getForgetCaptcha(phone);
            } else {
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }
        }
        return ResultBuilder.aResult().build();
    }


    /**
     * 手机获取登入验证码
     * @param phone
     * @return
     */
    @PostMapping("/getSmsLoginCaptcha")
    public Result getSmsLoginCaptcha(@RequestParam String phone) {
        if (null == phone || StringUtils.isEmpty(phone.replaceAll(" ", ""))) {
            ExceptionTool.throwException("请填写手机号!","7000");
        }else {
            phone = phone.replaceAll(" ", "");
            boolean isPhone = MyPatternUtil.validatePhoneNumber(phone);
            if (isPhone) {
                return smsService.getSmsLoginCaptcha(phone);
            } else {
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }
        }
        return ResultBuilder.aResult().build();
    }



    /**
     * 获取注册验证码
     * * @param phone
     * @return
     */
    @PostMapping("/getRegistationCaptcha")
    public Result getRegistationCaptcha(@RequestParam String phone) {
        if (null == phone || StringUtils.isEmpty(phone.replaceAll(" ", ""))) {
            ExceptionTool.throwException("请填写手机号!","7000");
        }else {
            phone = phone.replaceAll(" ", "");
            boolean isPhone = MyPatternUtil.validatePhoneNumber(phone);
            if (isPhone) {
                return smsService.getRegistationCaptcha(phone);
            } else {
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }
        }
        return ResultBuilder.aResult().build();
    }




    /**
     * 修改手机号时，获取修改手机号需要的验证码
     * @return
     *//*
    @RequestMapping("/sendUpdatePhoneCode")
    public JSONObject sendUpdatePhoneCode(@RequestParam String phoneNumber) {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject = smsService.sendUpdatePhoneCode(phoneNumber);
        } catch (Exception e) {
            resultObject.put("resultCode", 500);
            resultObject.put("resultMsg", "短信服务功能异常!");
        }
        return resultObject;
    }*/







}
