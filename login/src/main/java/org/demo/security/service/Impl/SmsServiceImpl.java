package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.qcloudsms.SmsSingleSenderResult;

import org.demo.security.authentication.config.RedisCache;
import org.demo.security.common.constant.TencentCloudConstant;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.MyPatternUtil;
import org.demo.security.common.web.util.TencentcloudSendMsgUtil;
import org.demo.security.dao.UserMapper;
import org.demo.security.entity.User;
import org.demo.security.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    @Value("${appID}")
    private int appID;
    @Value("${appKey}")
    private String appKey;
    @Value("${registration.verification.code.expiration}")
    private Integer registrationVerificationCodeExpiration;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;



    /**
     * 发送修改手机号时的验证码
     *
     * @return
     */
    /*@Transactional
    public Result sendUpdatePhoneCode(String phoneNumber) {
        JSONObject resultObject = new JSONObject();
        if (null == phoneNumber || "".equals(phoneNumber.replaceAll(" ", ""))) {
            return ResultBuilder.aResult().code("10001").msg("请填写手机号!").build();
        } else {
            phoneNumber = phoneNumber.replaceAll(" ", "");
            boolean isPhoneNumber = MyPatternUtil.validatePhoneNumber(phoneNumber);
            if (isPhoneNumber) {
                User user = userMapper.getUserByPhone(phoneNumber);
                if (null == user) {
                    return ResultBuilder.aResult().code("10001").msg("手机用户不存在!").build();
                } else {
                    Random random = new Random();
                    Integer number = random.nextInt(90000) + 10000;
                    String[] params = {String.valueOf(number)};
                    try {
                        SmsSingleSenderResult smsSingleSenderResult = TencentcloudSendMsgUtil.sendMsgRegistrationCode(phoneNumber, appID, appKey, TencentCloudConstant.FORGET_TEMPLATECODE, TencentcloudConstant.FORGET_SIGNNAME, params);
                        if(0==smsSingleSenderResult.result&&smsSingleSenderResult.errMsg.equals("OK")){
                            redisCache.setCacheObject(TencentCloudConstant.UPDATEPHONE_PRE + phoneNumber, number, registrationVerificationCodeExpiration, TimeUnit.SECONDS);
                            return ResultBuilder.aResult().code("10000").msg("验证码发送成功!").build();
                        }else {
                            return ResultBuilder.aResult().code("10001").msg(smsSingleSenderResult.errMsg).build();
                        }
                    } catch (Exception e) {
                        return ResultBuilder.aResult().code("10001").msg("验证码发送失败!").build();
                    }
                }
            } else {
                resultObject.put("resultCode", 10001);
                resultObject.put("resultMsg", "请正确填写手机号!");

            }
        }
        return resultObject;
    }*/
    /**
     * 忘记密码时，获取修改密码需要的验证码
     * @param phone
     * @return
     */
    @Transactional
    @Override
    public Result getForgetCaptcha(String phone) {
        User user = userMapper.getUserByPhone(phone);
        if (null == user) {
            ExceptionTool.throwException("手机号未注册!","7000");
        } else {
            Random random = new Random();
            Long number = random.nextLong(90000) + 10000;
            String[] params = {String.valueOf(number)};
            try {
                SmsSingleSenderResult smsSingleSenderResult = TencentcloudSendMsgUtil.sendMsgRegistrationCode(phone, appID, appKey, TencentCloudConstant.REGISTRATION_TEMPLATECODE, TencentCloudConstant.REGISTRATION_SIGNNAME, params);
                if(0==smsSingleSenderResult.result&&smsSingleSenderResult.errMsg.equals("OK")){
                    try {
                        redisCache.setCacheObject(TencentCloudConstant.FORGET_PRE + phone, number, registrationVerificationCodeExpiration, TimeUnit.SECONDS);
                        return ResultBuilder.aResult().code("2000").build();
                    } catch (Exception e) {
                        throw new RedisConnectionFailureException("系统错误");
                    }
                }else {
                    ExceptionTool.throwException("验证码发送失败","7000");
                }
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    ExceptionTool.throwException("系统错误","7000");
                }else {
                    ExceptionTool.throwException("验证码发送失败","7000");
                }
            }
        }
        return ResultBuilder.aResult().build();
    }


    /**
     * 手机获取登入验证码
     * @param phone
     * @return
     */
    @Transactional
    @Override
    public Result getSmsLoginCaptcha(String phone) {
        User user = userMapper.getUserByPhone(phone);
        if (null == user) {
            ExceptionTool.throwException("手机号未注册!","7000");
        } else {
            Random random = new Random();
            Long number = random.nextLong(90000) + 10000;
            String[] params = {String.valueOf(number)};
            try {
                SmsSingleSenderResult smsSingleSenderResult = TencentcloudSendMsgUtil.sendMsgRegistrationCode(phone, appID, appKey, TencentCloudConstant.REGISTRATION_TEMPLATECODE, TencentCloudConstant.REGISTRATION_SIGNNAME, params);
                if(0==smsSingleSenderResult.result&&smsSingleSenderResult.errMsg.equals("OK")){
                    try {
                        redisCache.setCacheObject(TencentCloudConstant.SMS_LOGIN_PRE + phone, number, registrationVerificationCodeExpiration, TimeUnit.SECONDS);
                        return ResultBuilder.aResult().code("2000").build();
                    } catch (Exception e) {
                        throw new RedisConnectionFailureException("系统错误");
                    }
                }else {
                    ExceptionTool.throwException("登录码发送失败","7000");
                }
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    ExceptionTool.throwException("系统错误","7000");
                }else {
                    ExceptionTool.throwException("登录码发送失败","7000");
                }
            }
        }
        return ResultBuilder.aResult().build();
    }



    /**
     * 获取手机注册验证码
     *@param phone
     * @return
     */
    @Transactional
    @Override
    public Result getRegistationCaptcha(String phone)  {


        User user = userMapper.getUserByPhone(phone);
        if (null != user) {
            ExceptionTool.throwException("手机号已被注册!","7000");
        } else {
            Random random = new Random();
            Long number = random.nextLong(90000) + 10000;
            String[] params = {String.valueOf(number)};
            try {
                SmsSingleSenderResult smsSingleSenderResult = TencentcloudSendMsgUtil.sendMsgRegistrationCode(phone, appID, appKey, TencentCloudConstant.REGISTRATION_TEMPLATECODE, TencentCloudConstant.REGISTRATION_SIGNNAME, params);
                if(0==smsSingleSenderResult.result&&smsSingleSenderResult.errMsg.equals("OK")){
                    try {
                        redisCache.setCacheObject(TencentCloudConstant.REGISTRATION_PRE + phone, number, registrationVerificationCodeExpiration, TimeUnit.SECONDS);
                        return ResultBuilder.aResult().code("2000").build();
                    } catch (Exception e) {
                        throw new RedisConnectionFailureException("系统错误");
                    }
                }else {
                    ExceptionTool.throwException("注册码发送失败","7000");
                }
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    ExceptionTool.throwException("系统错误","7000");
                }else {
                    ExceptionTool.throwException("注册码发送失败","7000");
                }
            }
        }
        return ResultBuilder.aResult().build();
    }




    /**
     * 忘记密码时，获取修改密码需要的验证码,腾讯云短信服务
     *
     * @return
     */
   /* public JSONObject sendForgetCode(String phoneNumber) {
        JSONObject resultObject = new JSONObject();
        if (null == phoneNumber || "".equals(phoneNumber.replaceAll(" ", ""))) {
            resultObject.put("resultCode", 10001);
            resultObject.put("resultMsg", "请填写手机号!");
        } else {
            phoneNumber = phoneNumber.replaceAll(" ", "");
            boolean isPhoneNumber = MyPatternUtil.validatePhoneNumber(phoneNumber);
            if (isPhoneNumber) {
                User user = userInteriorService.getUserByPhone(phoneNumber);
                if (null == user) {
                    resultObject.put("resultCode", 10001);
                    resultObject.put("resultMsg", "手机用户不存在!");
                } else {
                    Random random = new Random();
                    Integer number = random.nextInt(90000) + 10000;
                    String[] params = {String.valueOf(number)};
                    try {
                        SmsSingleSenderResult smsSingleSenderResult = TencentcloudSendMsgUtil.sendMsgRegistrationCode(phoneNumber, appID, appKey, TencentcloudConstant.FORGET_TEMPLATECODE, TencentcloudConstant.FORGET_SIGNNAME, params);
                        if(0==smsSingleSenderResult.result&&smsSingleSenderResult.errMsg.equals("OK")){
                            redisCacheUtil.setCacheObject(TencentcloudConstant.FORGET_PRE + phoneNumber, number, registrationVerificationCodeExpiration, TimeUnit.SECONDS);
                            resultObject.put("resultCode", 10000);
                            resultObject.put("resultMsg", "验证码发送成功!");
                        }else {
                            resultObject.put("resultCode", 10001);
                            resultObject.put("resultMsg", smsSingleSenderResult.errMsg);
                        }
                    } catch (Exception e) {
                        resultObject.put("resultCode", 10001);
                        resultObject.put("resultMsg", "验证码发送失败!");
                    }
                }
            } else {
                resultObject.put("resultCode", 10001);
                resultObject.put("resultMsg", "请正确填写手机号!");
            }
        }
        return resultObject;
    }*/


}
